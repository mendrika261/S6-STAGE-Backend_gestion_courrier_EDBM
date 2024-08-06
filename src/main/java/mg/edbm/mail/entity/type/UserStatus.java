package mg.edbm.mail.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    WORKING(0),
    SUSPENDED(-10);

    private final Integer code;
}
