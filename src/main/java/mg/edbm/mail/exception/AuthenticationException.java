package mg.edbm.mail.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
