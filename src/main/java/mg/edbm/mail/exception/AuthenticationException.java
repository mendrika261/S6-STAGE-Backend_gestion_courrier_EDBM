package mg.edbm.mail.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.exception.type.LogType;

@Getter
@Setter
@Log4j2
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, @NonNull LogType logType) {
        super(message);
        switch (logType) {
            case INFO -> log.info(message);
            case ERROR -> log.error(message);
            case WARN -> log.warn(message);
            case DEBUG -> log.debug(message);
            case TRACE -> log.trace(message);
        }
    }
}
