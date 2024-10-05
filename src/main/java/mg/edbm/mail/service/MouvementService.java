package mg.edbm.mail.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.properties.NotificationUrlProperties;
import mg.edbm.mail.dto.MapDto;
import mg.edbm.mail.dto.MessengerStatsDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.LogicOperationType;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.dto.request.type.SortType;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.MailPriority;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MouvementStatus;
import mg.edbm.mail.entity.type.NotificationType;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.MailRepository;
import mg.edbm.mail.repository.MouvementRepository;
import mg.edbm.mail.utils.StringCustomUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MouvementService {
    private final MailService mailService;
    private final MailRepository mailRepository;
    private final MouvementRepository mouvementRepository;
    private final NotificationService notificationService;
    private final NotificationUrlProperties notificationUrlProperties;

    public Mouvement createMouvement(UUID mailId, User messenger) throws NotFoundException {
        final Mail mail = mailService.get(mailId);
        final Mouvement mouvement = new Mouvement(mail, messenger);
        mail.addMouvement(mouvement);
        if(mouvement.getSenderUser() == null)
            mail.setStatus(MailStatus.DELIVERING);
        else {
            notificationService.create(NotificationType.DANGER,
                    "Passation courrier " + mail.getReference(),
                    "pris par " + StringCustomUtils.titleCase(messenger.getFirstName()),
                    notificationUrlProperties.MAIL_PREVIEW.replace(":mailId", mail.getId().toString()),
                    mouvement.getSenderUser());
        }
        mailRepository.save(mail);
        mailService.putDirections(mailId);
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
        listRequest.addBaseCriteria(LogicOperationType.AND, "senderUser.id", OperationType.EQUAL, userId);
        listRequest.addBaseCriteria(LogicOperationType.AND, "status", OperationType.EQUAL, MouvementStatus.WAITING);
        listRequest.addBaseCriteria(LogicOperationType.OR, "receiverUser.id", OperationType.EQUAL, userId);
        listRequest.addBaseCriteria(LogicOperationType.AND, "status", OperationType.EQUAL, MouvementStatus.DELIVERING);
        listRequest.addBaseCriteria(LogicOperationType.AND, "endDate", OperationType.IS_NOT_NULL, null);
        return list(listRequest);
    }

    public MessengerStatsDto getMessengerStats(User authenticatedUser) {
        final MessengerStatsDto messengerStatsDto = new MessengerStatsDto();
        messengerStatsDto.setUrgentCount(mouvementRepository.getUrgentCount(MouvementStatus.WAITING, MailPriority.URGENT));
        messengerStatsDto.setWaitingCount(mouvementRepository.getWaitingCount(MouvementStatus.WAITING));
        messengerStatsDto.setDeliveringCount(mouvementRepository.getDeliveringCount(MouvementStatus.DELIVERING, authenticatedUser));
        messengerStatsDto.setDeliveringTime(mouvementRepository.getDeliveringTime(MouvementStatus.DELIVERING, authenticatedUser));
        messengerStatsDto.setDeliveringDistance(mouvementRepository.getDeliveringDistance(MouvementStatus.DELIVERING, authenticatedUser));
        messengerStatsDto.setFirstDeliveringDatetime(mouvementRepository.getFirstDeliveringDatetime(MouvementStatus.DELIVERING, authenticatedUser));
        return messengerStatsDto;
    }

    public Page<Mouvement> getMessengerDeliveringMail(ListRequest listRequest, User authenticatedUser) {
        listRequest.addBaseCriteria("messenger.id", OperationType.EQUAL, authenticatedUser.getId());
        listRequest.addBaseCriteria(LogicOperationType.AND, "status", OperationType.EQUAL, MouvementStatus.DELIVERING);
        listRequest.addOrder("mail.createdAt", SortType.ASC);
        listRequest.addOrder("mail.priority", SortType.DESC);
        return list(listRequest);
    }

    public Page<Mouvement> list(ListRequest listRequest) {
        final Pageable pageable = listRequest.toPageable();
        final Specification<Mouvement> specification = new SpecificationImpl<>(listRequest);
        return mouvementRepository.findAll(specification, pageable);
    }
}
