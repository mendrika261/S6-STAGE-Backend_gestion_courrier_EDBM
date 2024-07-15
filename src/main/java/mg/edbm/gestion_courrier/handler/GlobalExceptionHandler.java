package mg.edbm.gestion_courrier.handler;


import mg.edbm.gestion_courrier.dto.response.Reponse;
import mg.edbm.gestion_courrier.dto.response.ValidationResponse;
import mg.edbm.gestion_courrier.exception.AuthentificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Reponse<ValidationResponse[]>> handleMethodArgumentNotValidExceptions(
            MethodArgumentNotValidException ex) {
        return Reponse.envoyer(Reponse.ERREUR, "Erreur de validation", ValidationResponse.liste(ex.getBindingResult()));
    }

    @ExceptionHandler(AuthentificationException.class)
    public ResponseEntity<Reponse<String>> handleAuthentificationExceptions(
            AuthentificationException ex) {
        return Reponse.envoyer(Reponse.NON_AUTHENTIFIE, ex.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Reponse<String>> handleAccessDeniedExceptions(
            AccessDeniedException ex) {
        return Reponse.envoyer(Reponse.ACCES_INTERDIT, "Accès interdit", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Reponse<String>> handleExceptions(Exception ex) {
        return Reponse.envoyer(Reponse.ERREUR_SERVEUR, ex.getMessage(), null);
    }
}
