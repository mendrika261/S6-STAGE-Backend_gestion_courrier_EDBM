package mg.edbm.mail.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mg.edbm.mail.dto.request.validator.PasswordMatches;
import org.hibernate.validator.constraints.Length;

@Data
@PasswordMatches
public class PasswordDtoRequest {
    @NotBlank(message = "L'ancien mot de passe est obligatoire")
    @Length(min = 8, max = 255, message = "L'ancien mot de passe doit contenir au moins 8 caractères")
    private String oldPassword = "";
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Length(min = 8, max = 255, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password = "";
    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    @Length(min = 8, max = 255, message = "La confirmation du mot de passe doit contenir 8 à 255 caractères")
    private String confirmPassword = "";
}
