package mg.edbm.mail.entity.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionStatus {
    INTRUSION(-30),
    TENTATIVE(-20),
    EXPIRED(-10),
    ACTIVE(0),
    FINISHED(10);

    private final Integer code;
}
