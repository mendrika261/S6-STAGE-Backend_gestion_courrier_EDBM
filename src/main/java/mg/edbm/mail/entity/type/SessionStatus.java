package mg.edbm.mail.entity.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionStatus {
    INTRUSION(-20),
    EXPIRED(-10),
    WORKING(0),
    FINISHED(10);

    private final Integer code;
}
