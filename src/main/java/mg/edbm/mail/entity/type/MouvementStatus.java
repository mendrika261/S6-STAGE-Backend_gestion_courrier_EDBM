package mg.edbm.mail.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MouvementStatus {
    CANCELLED(-10),
    WAITING(0),
    DELIVERING(10),
    DONE(20);
    private final Integer code;
}