package mg.edbm.gestion_courrier.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AuthentificationRequest {
    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    @Length(max = 255, message = "L'adresse email ne doit pas dépasser 255 caractères")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Length(max = 255, message = "Le mot de passe ne doit pas dépasser 255 caractères")
    private String motDePasse;

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }
}
