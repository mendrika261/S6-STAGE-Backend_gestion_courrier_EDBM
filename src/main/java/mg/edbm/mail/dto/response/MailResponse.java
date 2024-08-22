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
import java.util.Set;
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
    private Set<FileDto> files;

    public MailResponse(Mail mail) {
        setId(mail.getId());
        setReference(mail.getReference());
        setObject(mail.getObject());
        setConfidentiality(mail.getConfidentiality());
        setMailPriority(mail.getPriority());
        setStatus(mail.getStatus());
        setCreatedAt(mail.getCreatedAt());

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

        setFiles(mail.getFiles().stream().map(FileDto::new).collect(Collectors.toSet()));
        setNoteForMessenger(mail.getNoteForMessenger());
        setDescription(mail.getDescription());
    }
}
