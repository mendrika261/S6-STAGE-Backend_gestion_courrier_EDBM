package mg.edbm.mail.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.config.properties.FileUploadProperties;
import mg.edbm.mail.dto.request.MouvementRequest;
import mg.edbm.mail.dto.request.type.LogicOperationType;
import mg.edbm.mail.dto.request.FileUploadRequest;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.MailRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.MailType;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.dto.request.type.SortType;
import mg.edbm.mail.entity.*;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MouvementStatus;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.FileRepository;
import mg.edbm.mail.repository.MailRepository;
import mg.edbm.mail.repository.MouvementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailService {
    private static final Integer MAIL_REFERENCE_SEQ_LENGTH = 6;
    private final MailRepository mailRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final FileUploadProperties fileUploadProperties;
    private final MouvementRepository mouvementRepository;

    public Page<Mail> getMailsByUser(UUID userId, MailType type, ListRequest listRequest) {
        if (type == MailType.INCOMING)
            listRequest.addBaseCriteria("receiverUser", OperationType.EQUAL, userId);
        else
            listRequest.addBaseCriteria("senderUser", OperationType.EQUAL, userId);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }

    private String getNextReference(MailType type) {
        // --/2021-02-000001 OR --/2021-02-000001
        final String prefix = type.getReferencePrefix();
        final String year = String.valueOf(LocalDateTime.now().getYear());
        final String month = String.format("%02d", LocalDateTime.now().getMonthValue());
        final String lastReference = mailRepository.findLastReference(prefix);
        if (lastReference == null) {
            return prefix + year + "-" + month + "-" + String.format("%0"+ MAIL_REFERENCE_SEQ_LENGTH +"d", 1);
        }
        final String[] parts = lastReference.split("-");
        final int sequence = Integer.parseInt(parts[2]) + 1;
        return prefix + year + "-" + month + "-" + String.format("%0"+ MAIL_REFERENCE_SEQ_LENGTH +"d", sequence);
    }

    private Mail setReceiverMail(MailRequest mailRequest, Mail mail) throws NotFoundException {
        Location locationReceiver;
        String receiver;
        if (mailRequest.getReceiverUserId() != null) {
            final User receiverUser = userService.get(mailRequest.getReceiverUserId());
            mail.setReceiverUser(receiverUser);
            locationReceiver = receiverUser.getLocation();
            receiver = receiverUser.getFullName();
        } else {
            locationReceiver = locationService.get(mailRequest.getReceiverLocationId());
            receiver = mailRequest.getReceiver();
        }
        mail.setReceiverLocation(locationReceiver);
        mail.setReceiver(receiver);
        return mailRepository.save(mail);
    }

    public Mail createIncomingMail(UUID userId, MailRequest mailRequest, User authenticatedUser)
            throws NotFoundException {
        final User senderUser = userService.get(userId);
        Mail mail = new Mail(mailRequest, senderUser, authenticatedUser);

        mail.setReference(getNextReference(MailType.INCOMING));
        mail.setSender(mailRequest.getSender());
        mail.setSenderLocation(locationService.get(mailRequest.getSenderLocationId()));

        mail = setReceiverMail(mailRequest, mail);

        // First mouvement to reception
        final Mouvement mouvement = new Mouvement(mail, authenticatedUser);
        mouvement.setReceiverUser(authenticatedUser);
        mouvement.setReceiver(authenticatedUser.getFullName());
        mouvement.setReceiverLocation(authenticatedUser.getLocation());
        mouvement.setStatus(MouvementStatus.DONE);
        mouvement.setEndDate(LocalDateTime.now());
        mail.addMouvement(mouvement);
        return mailRepository.save(mail);
    }

    public Mail createOutgoingMail(UUID userId, MailRequest mailRequest, User authenticatedUser)
            throws NotFoundException {
        final User senderUser = userService.get(userId);
        final Mail mail = new Mail(mailRequest, senderUser, authenticatedUser);

        mail.setReference(getNextReference(MailType.OUTGOING));

        return setReceiverMail(mailRequest, mail);
    }

    public Mail createMail(UUID userId, MailRequest mailRequest, User authenticatedUser)
            throws NotFoundException {
        if (authenticatedUser.hasRole(SecurityConfig.ROLE_RECEPTIONIST)) {
            return createIncomingMail(userId, mailRequest, authenticatedUser);
        } else {
            return createOutgoingMail(userId, mailRequest, authenticatedUser);
        }
    }

    public Mail get(UUID mailId) throws NotFoundException {
        return mailRepository.findById(mailId).orElseThrow(
                () -> new NotFoundException("Le courrier n'existe pas"));
    }

    public Page<File> listFiles(UUID mailId, ListRequest listRequest) throws NotFoundException {
        final Mail mail = get(mailId);
        listRequest.addBaseCriteria("mail", OperationType.EQUAL, mail);
        final Specification<File> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return fileRepository.findAll(specification, pageable);
    }

    public File uploadMailFile(UUID mailId, FileUploadRequest fileUploadRequest, User authenticatedUser) throws IOException {
        final Mail mail = getIfSendBy(mailId, authenticatedUser);

        final File file = new File(fileUploadRequest.getFile(), authenticatedUser);
        file.setPath(fileUploadProperties.UPLOAD_DIR + "/" + file.getId());
        fileService.storeFile(fileUploadRequest.getFile(), file.getPath());
        mail.addFile(file);
        mailRepository.save(mail);
        return file;
    }

    public Mail deleteMailFileSendBy(UUID mailId, UUID fileId, User author) throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, author);
        final File file = fileRepository.findById(fileId).orElseThrow(
                () -> new NotFoundException("File with id #" + fileId + " not found"));

        try {
            mail.removeFile(file);
            mailRepository.save(mail);
            fileService.deleteStoredFile(file);
            fileRepository.delete(file);
        } catch (IOException e) {
            log.error("Error while deleting on storage file with id #{}", fileId, e);
        }
        return mail;
    }

    public void deleteAllMailFilesSendBy(UUID mailId, User author) throws NotFoundException {
        for (File file : getIfSendBy(mailId, author).getFiles()) {
            deleteMailFileSendBy(mailId, file.getId(), author);
        }
    }

    public Mail getIfSendBy(UUID mailId, User senderUser) {
        return mailRepository.findByIdAndSenderUser(mailId, senderUser).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    public Mail getMailByUser(UUID userId, UUID mailId) throws NotFoundException {
        final User user = userService.get(userId);
        return mailRepository.findByIdAndReceiverOrSenderUser(mailId, user).orElseThrow(
                () -> new NotFoundException("Le courrier n'existe pas")
        );
    }

    private Mail updateOutgoingMail(UUID userId, UUID mailId, @Valid MailRequest mailRequest,
                                   User authenticatedUser) throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, userService.get(userId));
        mail.updateIfAuthorized(mailRequest, authenticatedUser);

        return mailRepository.save(mail);
    }

    private Mail updateIncomingMail(UUID userId, UUID mailId, @Valid MailRequest mailRequest,
                                   User authenticatedUser) throws NotFoundException {
        final Mail mail = updateOutgoingMail(userId, mailId, mailRequest, authenticatedUser);
        mail.setSender(mailRequest.getSender());
        mail.setSenderLocation(locationService.get(mailRequest.getSenderLocationId()));
        return mailRepository.save(mail);
    }

    public Mail updateMail(UUID userId, UUID mailId, @Valid MailRequest mailRequest,
                           User authenticatedUser) throws NotFoundException {
        if (authenticatedUser.hasRole(SecurityConfig.ROLE_RECEPTIONIST)) {
            return updateIncomingMail(userId, mailId, mailRequest, authenticatedUser);
        } else {
            return updateOutgoingMail(userId, mailId, mailRequest, authenticatedUser);
        }
    }

    public Mail updateMailStatus(UUID userId, UUID mailId, MailStatus mailStatus, User authenticatedUser)
            throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, userService.get(userId));
        mail.updateStatusIfAuthorized(mailStatus, authenticatedUser);
        return mailRepository.save(mail);
    }

    public Mail deleteMail(UUID userId, UUID mailId) throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, userService.get(userId));
        deleteAllMailFilesSendBy(mailId, userService.get(userId));
        mailRepository.delete(mail);
        return mail;
    }

    public Page<Mail> listWaitingMails(@Valid ListRequest listRequest) {
        listRequest.addBaseCriteria(LogicOperationType.AND, "status", OperationType.EQUAL, MailStatus.WAITING); // TODO make waiting when done
        listRequest.addOrder("createdAt", SortType.ASC);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }

    private Mail getIfMouvementSendingBy(UUID mailId, User senderUser) {
        return mailRepository.findByIdAndMouvementsSenderUser(mailId, senderUser).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    public Mail signMouvementStart(UUID mailId, LocalDateTime startDate, User author) {
        final Mail mail = getIfMouvementSendingBy(mailId, author);
        mail.signMouvementStart(startDate);
        return mailRepository.save(mail);
    }

    public Page<Mail> listDeliveringMails(@Valid ListRequest listRequest, User messenger) {
        listRequest.addBaseCriteria("mouvements.messenger", OperationType.EQUAL, messenger.getId());
        listRequest.addBaseCriteria("mouvements.status", OperationType.EQUAL, MouvementStatus.DELIVERING);
        listRequest.addOrder("mouvements.startDate", SortType.ASC);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }

    private Mail getIfMouvementDeliveringBy(UUID mailId, User messenger) {
        return mailRepository.findByIdAndMouvementsMessengerAndMouvementsStatus(mailId, messenger, MouvementStatus.DELIVERING).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    private Mail getIfMouvementReceivedBy(UUID mailId, User receiverUser) {
        return mailRepository.findByIdAndMouvementsReceiverUser(mailId, receiverUser).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    public Mail deliverMail(UUID mailId, LocalDateTime endDate, User messenger) {
        final Mail mail = getIfMouvementDeliveringBy(mailId, messenger);
        mail.deliverMail(endDate);
        return mailRepository.save(mail);
    }

    public Mail confirmDelivery(UUID mailId, LocalDateTime endDate, User receiverUser) {
        final Mail mail = getIfMouvementReceivedBy(mailId, receiverUser);
        mail.confirmDelivery(endDate);
        return mailRepository.save(mail);
    }

    public Mouvement reroute(UUID mailId, MouvementRequest mouvementRequest, User messenger) throws NotFoundException {
        final Mail mail = getIfMouvementDeliveringBy(mailId, messenger);
        final Mouvement mouvement = mail.getLastMouvement();
        if(mouvementRequest.getReceiverUserId() == null) {
            mouvement.setReceiverUser(null);
            mouvement.setReceiver(mouvementRequest.getReceiver());
            mouvement.setReceiverLocation(locationService.get(mouvementRequest.getReceiverLocationId()));
        } else {
            final User user = userService.get(mouvementRequest.getReceiverUserId());
            mouvement.setReceiverUser(user);
            mouvement.setReceiver(user.getFullName());
            mouvement.setReceiverLocation(user.getLocation());
        }
        return mouvementRepository.save(mouvement);
    }

    public Mail cancelDelivery(UUID mailId, User messenger) {
        final Mail mail = getIfMouvementDeliveringBy(mailId, messenger);
        mail.setStatus(MailStatus.DELIVERING);
        mail.getLastMouvement().setStatus(MouvementStatus.DELIVERING);
        mail.getLastMouvement().setEndDate(null);
        return mailRepository.save(mail);
    }

}
