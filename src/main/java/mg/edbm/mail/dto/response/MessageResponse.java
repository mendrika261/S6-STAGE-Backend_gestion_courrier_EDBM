package mg.edbm.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.mail.dto.response.type.MessageType;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private MessageType messageType = MessageType.ERROR;

    public MessageResponse(String message) {
        setMessage(message);
    }
}
