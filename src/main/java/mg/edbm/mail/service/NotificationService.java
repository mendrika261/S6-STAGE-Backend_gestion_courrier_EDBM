package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.dto.request.type.SortType;
import mg.edbm.mail.entity.Notification;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.NotificationType;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Page<Notification> list(ListRequest listRequest, User user) {
        listRequest.addBaseCriteria("doneAt", OperationType.IS_NULL, null);
        listRequest.addBaseCriteria("user.id", OperationType.EQUAL, user.getId());
        listRequest.addOrder("createdAt", SortType.DESC);
        final Pageable pageable = listRequest.toPageable();
        final Specification<Notification> specification = new SpecificationImpl<>(listRequest);
        return notificationRepository.findAll(specification, pageable);
    }

    public Notification get(UUID id) throws NotFoundException {
        return notificationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("La notification #" + id + " n'existe pas")
        );
    }

    public Notification markAsDone(UUID id) throws NotFoundException {
        final Notification notification = get(id);
        notification.markAsDone();
        return notificationRepository.save(notification);
    }

    public Notification create(NotificationType type, String title, String description, String url, User user) {
        final Notification notification = new Notification(type, title, description, url, user);
        return notificationRepository.save(notification);
    }
}
