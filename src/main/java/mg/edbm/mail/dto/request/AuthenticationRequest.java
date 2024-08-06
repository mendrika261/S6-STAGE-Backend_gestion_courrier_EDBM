package mg.edbm.mail.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AuthenticationRequest {
    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    @Length(max = 255, message = "L'adresse email ne doit pas dépasser 255 caractères")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Length(max = 255, message = "Le mot de passe ne doit pas dépasser 255 caractères")
    private String password;

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }
}
