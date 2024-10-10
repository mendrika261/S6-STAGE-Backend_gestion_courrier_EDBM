package mg.edbm.mail.dto.request.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailType {
    INCOMING("REC"),
    OUTGOING("ENV");
    private final String referencePrefix;
}
