package mg.edbm.gestion_courrier.dto.response;

import lombok.Data;

@Data
public class RaisonResponse {
    String[] messages;

    public static RaisonResponse liste(String... message) {
        RaisonResponse raisonResponse = new RaisonResponse();
        raisonResponse.setMessages(message);
        return raisonResponse;
    }
}
