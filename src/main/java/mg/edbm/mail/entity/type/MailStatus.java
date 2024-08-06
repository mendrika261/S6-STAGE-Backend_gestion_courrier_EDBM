package mg.edbm.mail.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailStatus {
    CANCELED(-10),
    WAITING(0),
    DELIVERING(10),
    DONE(20);

    private final Integer code;
}
