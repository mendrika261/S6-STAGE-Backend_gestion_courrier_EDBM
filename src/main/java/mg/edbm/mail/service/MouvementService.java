package mg.edbm.mail.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.LogicOperationType;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.entity.File;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MouvementStatus;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.MailRepository;
import mg.edbm.mail.repository.MouvementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MouvementService {
    private final MailService mailService;
    private final MailRepository mailRepository;
    private final MouvementRepository mouvementRepository;

    public Mouvement createMouvement(UUID mailId, User messenger) throws NotFoundException {
        final Mail mail = mailService.get(mailId);
        final Mouvement mouvement = new Mouvement(mail, messenger);
        mail.addMouvement(mouvement);
        if(mouvement.getSenderUser() == null)
            mail.setStatus(MailStatus.DELIVERING);
        mailRepository.save(mail);
        return mouvement;
    }

    public Mouvement removeLastMouvement(UUID mailId, User messenger) throws NotFoundException {
        final Mail mail = mailService.get(mailId);
        final Mouvement mouvement = mail.getLastMouvement();
        if(!mouvement.getMessenger().getId().equals(messenger.getId()))
            throw new NotFoundException("The last mouvement is not from the messenger");
        mouvementRepository.delete(mouvement);
        return mouvement;
    }

    public Page<Mouvement> listTransitMails(@Valid ListRequest listRequest, UUID userId) {
        listRequest.addBaseCriteria(LogicOperationType.AND, "senderUser", OperationType.EQUAL, userId);
        listRequest.addBaseCriteria(LogicOperationType.AND, "status", OperationType.EQUAL, MouvementStatus.WAITING);
        listRequest.addBaseCriteria(LogicOperationType.OR, "receiverUser", OperationType.EQUAL, userId);
        listRequest.addBaseCriteria(LogicOperationType.AND, "status", OperationType.EQUAL, MouvementStatus.DELIVERING);
        listRequest.addBaseCriteria(LogicOperationType.AND, "endDate", OperationType.IS_NOT_NULL, null);
        final Specification<Mouvement> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mouvementRepository.findAll(specification, pageable);
    }
}
