package mg.edbm.mail.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    DANGER(-20),
    WARNING(-10),
    INFO(0),
    SUCCESS(10);
    private final Integer code;
}
