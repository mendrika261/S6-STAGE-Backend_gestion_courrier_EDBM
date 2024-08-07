package mg.edbm.mail.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    DISABLE(-20),
    SUSPENDED(-10),
    ACTIVE(0);

    private final Integer code;
}
