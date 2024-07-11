package mg.edbm.gestion_courrier.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthentificationException extends Exception {
    public AuthentificationException(String message) {
        super(message);
    }
}
