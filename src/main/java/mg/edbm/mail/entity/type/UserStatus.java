package mg.edbm.mail.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    DISABLE(-20),
    SUSPENDED(-10),
    CREATED(0),
    PENDING(10),
    ACTIVE(20);

    private final Integer code;
}
