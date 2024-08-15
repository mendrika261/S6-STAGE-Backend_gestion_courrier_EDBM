package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.MailOutgoingRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.MailType;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.entity.File;
import mg.edbm.mail.entity.Location;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.FileRepository;
import mg.edbm.mail.repository.MailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private static final Integer MAIL_REFERENCE_SEQ_LENGTH = 6;
    private final MailRepository mailRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final FileRepository fileRepository;

    public Page<Mail> getMailByUser(UUID userId, MailType type, ListRequest listRequest) throws NotFoundException {
        if (type == MailType.INCOMING) {
            listRequest.addCriteria("receiverUser", OperationType.EQUAL, userService.get(userId));
        } else {
            listRequest.addCriteria("senderUser", OperationType.EQUAL, userService.get(userId));
        }
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest.getCriteria());
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
        mail.setReference(getNextReference(MailType.OUTGOING));
        try {
            if(mailOutgoingRequest.getReceiverUserId() == null)
                throw new NotFoundException("Le destinataire n'existe pas");
            final User receiverUser = userService.get(UUID.fromString(mailOutgoingRequest.getReceiverUserId()));
            mail.setReceiverUser(receiverUser);
        } catch (NotFoundException e) {
            final Location location = locationService.get(Long.valueOf(mailOutgoingRequest.getReceiverLocationId()));
            mail.setReceiverLocation(location);
            mail.setReceiver(mailOutgoingRequest.getReceiver());
        }
        return mailRepository.save(mail);
    }

    public Mail get(UUID mailId) throws NotFoundException {
        return mailRepository.findById(mailId).orElseThrow(
                () -> new NotFoundException("Le courrier n'existe pas"));
    }

    public Page<File> listFiles(UUID mailId, ListRequest listRequest) throws NotFoundException {
        final Mail mail = get(mailId);
        listRequest.addCriteria("mail", OperationType.EQUAL, mail);
        final Specification<File> specification = new SpecificationImpl<>(listRequest.getCriteria());
        final Pageable pageable = listRequest.toPageable();
        return fileRepository.findAll(specification, pageable);
    }

    public Mail getIfSendBy(UUID mailId, User senderUser) {
        return mailRepository.findByIdAndSenderUser(mailId, senderUser).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }
}
