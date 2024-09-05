package mg.edbm.mail.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MouvementRequest {
    private String receiver;
    private UUID receiverUserId;
    private Long receiverLocationId;

    public void setReceiver(String receiver) {
        this.receiver = receiver.trim();
    }
}
