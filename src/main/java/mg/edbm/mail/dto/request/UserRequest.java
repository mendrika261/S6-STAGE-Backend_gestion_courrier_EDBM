package mg.edbm.mail.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@Data
public class UserRequest {
    private UUID id;
    @NotBlank(message = "Le nom est obligatoire")
    @Length(min = 2, max = 255, message = "Le nom doit contenir entre 2 et 255 caractères")
    private String lastName;
    @NotBlank(message = "Le prénom est obligatoire")
    @Length(min = 2, max = 255, message = "Le prénom doit contenir entre 2 et 255 caractères")
    private String firstName;
    @NotBlank(message = "L'adresse email est obligatoire")
    @Length(min = 2, max = 255, message = "L'adresse email doit contenir entre 2 et 255 caractères")
    @Email(message = "L'adresse email doit être valide")
    private String email;
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Length(min = 2, max = 255, message = "Le numéro de téléphone doit contenir entre 2 et 255 caractères")
    @Pattern(regexp = "^(03[234789]\\d{7}|020(22|26)\\d{5})$",
            message = "Le numéro de téléphone doit être au format 03[234789]XXXXXXX ou 020(22|26)XXXXXX")
    private String phoneNumber;
    @NotEmpty(message = "Le rôle est obligatoire")
    private List<Long> roles;
    @NotNull(message = "La localisation est obligatoire")
    private Long locationId;

    private String password = "";
    private Boolean passwordGenerated = false;

    public void setLastName(String lastName) {
        this.lastName = lastName.trim().toLowerCase();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim().toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim().toLowerCase();
    }
}
