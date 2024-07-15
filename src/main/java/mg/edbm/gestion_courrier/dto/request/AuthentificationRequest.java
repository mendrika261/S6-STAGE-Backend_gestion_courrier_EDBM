package mg.edbm.gestion_courrier.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthentificationRequest {
    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "L'adresse email est invalide")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
}
