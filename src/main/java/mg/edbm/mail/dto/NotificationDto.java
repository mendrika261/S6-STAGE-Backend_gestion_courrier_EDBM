package mg.edbm.mail.dto;

import lombok.Data;
import mg.edbm.mail.entity.Notification;
import mg.edbm.mail.entity.type.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationDto {
    private UUID id;
    private UUID userId;
    private String title;
    private String description;
    private String url;
    private NotificationType type;
    private LocalDateTime createdAt;

    public NotificationDto(Notification notification) {
        setId(notification.getId());
        setUserId(notification.getUser().getId());
        setTitle(notification.getTitle());
        setDescription(notification.getDescription());
        setUrl(notification.getUrl());
        setType(notification.getType());
        setCreatedAt(notification.getCreatedAt());
    }
}
