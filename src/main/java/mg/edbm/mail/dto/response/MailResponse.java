package mg.edbm.mail.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import mg.edbm.mail.dto.FileDto;
import mg.edbm.mail.dto.LocationDto;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.type.MailConfidentiality;
import mg.edbm.mail.entity.type.MailPriority;
import mg.edbm.mail.entity.type.MailStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class MailResponse {
    private UUID id;
    private String reference;
    private String object;
    private MailConfidentiality confidentiality;
    private MailPriority mailPriority;
    private MailStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocationDto senderLocation;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sender;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse senderUser;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocationDto receiverLocation;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiver;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse receiverUser;
    private String noteForMessenger;
    private String description;
    private LocalDateTime createdAt;
    private UserResponse createdBy;
    private List<FileDto> files;
    private List<MouvementResponse> mouvements;

    public MailResponse(Mail mail, boolean withMouvements) {
        setId(mail.getId());
        setReference(mail.getReference());
        setObject(mail.getObject());
        setConfidentiality(mail.getConfidentiality());
        setMailPriority(mail.getPriority());
        setStatus(mail.getStatus());
        setCreatedAt(mail.getCreatedAt());
        if(mail.getCreatedBy() !=null)
            setCreatedBy(new UserResponse(mail.getCreatedBy()));

        if(mail.getSenderLocation() != null)
            setSenderLocation(new LocationDto(mail.getSenderLocation()));
        if(mail.getSenderUser() != null)
            setSenderUser(new UserResponse(mail.getSenderUser()));
        setSender(mail.getSender());

        if (mail.getReceiverLocation() != null)
            setReceiverLocation(new LocationDto(mail.getReceiverLocation()));
        if (mail.getReceiverUser() != null)
            setReceiverUser(new UserResponse(mail.getReceiverUser()));
        setReceiver(mail.getReceiver());

        setFiles(mail.getFiles().stream().map(FileDto::new).collect(Collectors.toList()));
        setNoteForMessenger(mail.getNoteForMessenger());
        setDescription(mail.getDescription());
        if(withMouvements)
            setMouvements(mail.getMouvements().stream().map(MouvementResponse::new).collect(Collectors.toList()));
    }

    public MailResponse(Mail mail) {
        this(mail, true);
    }
}
