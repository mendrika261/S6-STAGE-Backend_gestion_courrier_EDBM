package mg.edbm.mail.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailPriority {
    NORMAL(0),
    URGENT(10);

    private final Integer code;
}
