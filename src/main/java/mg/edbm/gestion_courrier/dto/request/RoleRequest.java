package mg.edbm.gestion_courrier.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RoleRequest {
    @NotBlank(message = "Le nom du role est obligatoire")
    @Length(max = 255, message = "Le nom du role ne doit pas dépasser 255 caractères")
    private String nom;
    @NotBlank(message = "Le code du role est obligatoire")
    @Length(max = 255, message = "Le code du role ne doit pas dépasser 255 caractères")
    private String code;
}
