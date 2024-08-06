package mg.edbm.mail.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends Exception {
    private String[] fields = new String[0];

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String... fields) {
        super(message);
        setFields(fields);
    }
}
