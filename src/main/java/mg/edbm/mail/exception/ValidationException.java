package mg.edbm.mail.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
