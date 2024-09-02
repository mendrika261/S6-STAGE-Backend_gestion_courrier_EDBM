package mg.edbm.mail.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.config.properties.FileUploadProperties;
import mg.edbm.mail.dto.request.FileUploadRequest;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.MailOutgoingRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.MailType;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.dto.request.type.SortType;
import mg.edbm.mail.entity.*;
import mg.edbm.mail.entity.type.MailStatus;
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

    public Mail createOutgoingMail(UUID userId, MailOutgoingRequest mailOutgoingRequest, User authenticatedUser)
            throws NotFoundException {
        final User senderUser = userService.get(userId);
        final Mail mail = new Mail(mailOutgoingRequest, senderUser, authenticatedUser);
        Location location; String receiver;
        if (mailOutgoingRequest.getReceiverUserId() != null) {
            final User receiverUser = userService.get(UUID.fromString(mailOutgoingRequest.getReceiverUserId()));
            mail.setReceiverUser(receiverUser);
            location = receiverUser.getLocation();
            receiver = receiverUser.getFullName();
        } else {
            location = locationService.get(Long.valueOf(mailOutgoingRequest.getReceiverLocationId()));
            receiver = mailOutgoingRequest.getReceiver();
        }
        mail.setReference(getNextReference(MailType.OUTGOING));
        mail.setReceiverLocation(location);
        mail.setReceiver(receiver);
        return mailRepository.save(mail);
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

    public Mail updateOutgoingMail(UUID userId, UUID mailId, @Valid MailOutgoingRequest mailOutgoingRequest,
                                   User authenticatedUser) throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, userService.get(userId));
        mail.updateIfAuthorized(mailOutgoingRequest, authenticatedUser);
        return mailRepository.save(mail);
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
        listRequest.addBaseCriteria("status", OperationType.EQUAL, MailStatus.WAITING);
        listRequest.addOrder("createdAt", SortType.ASC);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }

    public Mail signMouvementStart(UUID mailId, LocalDateTime startDate, User author) {
        final Mail mail = getIfSendBy(mailId, author);
        mail.signMouvementStart(startDate, author);
        return mailRepository.save(mail);
    }
}
