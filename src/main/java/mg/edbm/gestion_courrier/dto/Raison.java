package mg.edbm.gestion_courrier.dto;

import lombok.Data;

@Data
public class Raison {
    String[] messages;

    public static Raison liste(String... message) {
        Raison raison = new Raison();
        raison.setMessages(message);
        return raison;
    }
}
