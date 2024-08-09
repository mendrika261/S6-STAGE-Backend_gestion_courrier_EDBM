package mg.edbm.mail.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.type.MailConfidentiality;
import mg.edbm.mail.entity.type.MailPriority;
import mg.edbm.mail.entity.type.MailStatus;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class MailDto {
    private UUID id;
    private String reference;
    private String object;
    private MailConfidentiality confidentiality;
    private MailPriority mailPriority;
    private MailStatus status;
    private LocationDto senderLocation;
    private String sender;
    private LocationDto receiverLocation;
    private String receiver;
    private Set<FileDto> files;

    public MailDto(Mail mail) {
        setId(mail.getId());
        setReference(mail.getReference());
        setObject(mail.getObject());
        setConfidentiality(mail.getConfidentiality());
        setMailPriority(mail.getPriority());
        setStatus(mail.getStatus());
        setSenderLocation(new LocationDto(mail.getSenderLocation()));
        setSender(mail.getSender());
        setReceiverLocation(new LocationDto(mail.getReceiverLocation()));
        setReceiver(mail.getReceiver());
        setFiles(mail.getFiles().stream().map(FileDto::new).collect(Collectors.toSet()));
    }
}
