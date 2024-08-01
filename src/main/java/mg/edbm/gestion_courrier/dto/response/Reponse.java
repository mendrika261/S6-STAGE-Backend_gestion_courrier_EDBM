package mg.edbm.gestion_courrier.dto.response;

import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
public class Reponse<T> {
    public final static int OK = 200;
    public final static int ERREUR = 400;
    public final static int INTROUVABLE = 404;
    public final static int NON_AUTHENTIFIE = 401;
    public final static int ACCES_INTERDIT = 403;
    public final static int ERREUR_SERVEUR = 500;
    private String message = null;
    private MessageType messageType = MessageType.info;
    private T data = null;

    public Reponse(String message, MessageType messageType, T data) {
        setData(data);
        setMessage(message);
        setMessageType(messageType);
    }

    public static <T> ResponseEntity<Reponse<T>> envoyer(int status, Reponse<T> data) {
        return ResponseEntity.status(status).body(data);
    }

    public static <T> ResponseEntity<Reponse<T>> envoyer(Reponse<T> data) {
        return ResponseEntity.status(OK).body(data);
    }

    public static <T> ResponseEntity<Reponse<T>> envoyer(int status, String message, MessageType messageType, T data) {
        return ResponseEntity.status(status).body(new Reponse<T>(message, messageType, data));
    }

    public static <T> ResponseEntity<Reponse<T>> envoyer(String message, MessageType messageType, T data) {
        return ResponseEntity.status(OK).body(new Reponse<T>(message, messageType, data));
    }

    public static <T> ResponseEntity<Reponse<T>> envoyer(int status, MessageType messageType, T data) {
        return ResponseEntity.status(status).body(new Reponse<T>(null, messageType, data));
    }

    public static <T> ResponseEntity<Reponse<T>> envoyer(MessageType messageType, T data) {
        return ResponseEntity.status(OK).body(new Reponse<T>(null, messageType, data));
    }
}
