package mg.edbm.mail.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailConfidentiality {
    PUBLIC(0),
    PRIVATE(10),
    SECRET(20);

    private final Integer code;
}
