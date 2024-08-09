package mg.edbm.mail.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mg.edbm.mail.dto.request.validator.ReceiverUserOrNameAndLocation;
import mg.edbm.mail.entity.type.MailConfidentiality;
import mg.edbm.mail.entity.type.MailPriority;

import org.hibernate.validator.constraints.Length;

@Data
@ReceiverUserOrNameAndLocation
public class MailOutgoingRequest {
    @NotBlank(message = "L'objet du mail est obligatoire")
    @Length(max = 255, message = "L'objet du mail ne peut pas dépasser 255 caractères")
    private String object;
    @NotNull(message = "La confidentialité du mail est obligatoire")
    private MailConfidentiality confidentiality;
    @NotNull(message = "La priorité du mail est obligatoire")
    private MailPriority mailPriority;
    @Length(max = 255, message = "Le destinataire du mail ne peut pas dépasser 255 caractères")
    private String receiver;
    private String receiverUserId;
    private String receiverLocationId;
    @Length(max = 255, message = "La note pour le coursier ne peut pas dépasser 255 caractères")
    private String noteForMessenger;
    @Length(max = 255, message = "La description du mail ne peut pas dépasser 255 caractères")
    private String description;

    public void setObject(String object) {
        this.object = object.trim();
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver.trim();
    }

    public void setNoteForMessenger(String noteForMessenger) {
        this.noteForMessenger = noteForMessenger.trim();
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }
}
