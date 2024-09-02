package mg.edbm.mail.dto;

import lombok.Data;
import mg.edbm.mail.dto.response.UserResponse;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.type.MouvementStatus;

import java.time.LocalDateTime;

@Data
public class MouvementDto {
    private Long id;
    private UserResponse messenger;
    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate;
    private LocationDto senderLocation;
    private String sender;
    private UserResponse senderUser;
    private LocationDto receiverLocation;
    private String receiver;
    private UserResponse receiverUser;
    private MouvementStatus status;
    private String description;

    public MouvementDto(Mouvement mouvement) {
        setId(mouvement.getId());
        setStartDate(mouvement.getStartDate());
        setEndDate(mouvement.getEndDate());
        setDescription(mouvement.getDescription());
        setStatus(mouvement.getStatus());

        if(mouvement.getSenderLocation() != null)
            setSenderLocation(new LocationDto(mouvement.getSenderLocation()));
        if(mouvement.getSenderUser() != null)
            setSenderUser(new UserResponse(mouvement.getSenderUser()));
        setSender(mouvement.getSender());

        if (mouvement.getReceiverLocation() != null)
            setReceiverLocation(new LocationDto(mouvement.getReceiverLocation()));
        if (mouvement.getReceiverUser() != null)
            setReceiverUser(new UserResponse(mouvement.getReceiverUser()));
        setReceiver(mouvement.getReceiver());

        if (mouvement.getMessenger() != null)
            setMessenger(new UserResponse(mouvement.getMessenger()));
    }
}
