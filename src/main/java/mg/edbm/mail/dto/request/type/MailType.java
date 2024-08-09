package mg.edbm.mail.dto.request.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailType {
    INCOMING("IN"),
    OUTGOING("OUT");
    private final String referencePrefix;
}
