package mg.edbm.mail.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import mg.edbm.mail.dto.LocationDto;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.type.MouvementStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MouvementResponse {
    private Long id;
    private UserResponse messenger;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocationDto senderLocation;
    private String sender;
    private UserResponse senderUser;
    private LocationDto receiverLocation;
    private String receiver;
    private UserResponse receiverUser;
    private MouvementStatus status;
    private String description;
    private String referenceMail;
    private MailResponse mail;
    private Double estimatedDistance;
    private Double estimatedDelay;
    private List<Double[]> coordinates = new ArrayList<>();

    public MouvementResponse(Mouvement mouvement) {
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

        setReferenceMail(mouvement.getMail().getReference());
        setMail(new MailResponse(mouvement.getMail(), false));

        setEstimatedDistance(mouvement.getEstimatedDistance());
        setEstimatedDelay(mouvement.getEstimatedDelay());
        setCoordinates(mouvement.getCoordinatesAsList());
    }
}
