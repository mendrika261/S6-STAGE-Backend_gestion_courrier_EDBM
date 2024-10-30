package mg.edbm.mail.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.NotificationDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.entity.Notification;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.service.NotificationService;
import mg.edbm.mail.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@Log4j2
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<NotificationDto>> list(ListRequest listRequest) throws AuthenticationException {
        final Page<Notification> notifications = notificationService.list(listRequest, userService.getAuthenticatedUser());
        return ResponseEntity.ok(notifications.map(NotificationDto::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationDto> markAsDone(@PathVariable UUID id) throws NotFoundException {
        final Notification notification = notificationService.markAsDone(id);
        return ResponseEntity.ok(new NotificationDto(notification));
    }
}
