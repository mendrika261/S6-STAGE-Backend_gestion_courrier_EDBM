package mg.edbm.mail.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@Data
public class UserProfileRequest {
    private UUID id;
    @NotBlank(message = "Le nom est obligatoire")
    @Length(min = 2, max = 255, message = "Le nom doit contenir entre 2 et 255 caractères")
    private String lastName;
    @NotBlank(message = "Le prénom est obligatoire")
    @Length(min = 2, max = 255, message = "Le prénom doit contenir entre 2 et 255 caractères")
    private String firstName;
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Length(min = 2, max = 255, message = "Le numéro de téléphone doit contenir entre 2 et 255 caractères")
    @Pattern(regexp = "^(03[234789]\\d{7}|020(22|26)\\d{5})$",
            message = "Le numéro de téléphone doit être au format 03[234789]XXXXXXX ou 020(22|26)XXXXXX")
    private String phoneNumber;
    @NotEmpty(message = "Un utilisateur doit avoir au moins un rôle")
    private List<String> roles;
    @NotNull(message = "La localisation est obligatoire")
    private Long locationId;

    public void setLastName(String lastName) {
        this.lastName = lastName.trim().toLowerCase();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim().toLowerCase();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim().toLowerCase();
    }
}
