package mg.edbm.mail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.edbm.mail.entity.Role;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class RoleDto {
    private Long id;
    @NotBlank(message = "Le nom du role est obligatoire")
    @Length(min = 1, max = 255, message = "Le nom du role doit être compris entre 1 et 255 caractères")
    private String name;
    @NotBlank(message = "Le code du role est obligatoire")
    @Length(min = 1, max = 255, message = "Le code du role doit être compris entre 1 et 255 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Le code du role ne doit contenir que des lettres, des chiffres et des underscores")
    private String code;
    private String color;

    public RoleDto(Role role) {
        setId(role.getId());
        setName(role.getName());
        setCode(role.getCode());
        setColor(role.getColor());
    }

    public void setName(String name) {
        this.name = name.trim().toLowerCase();
    }

    public void setCode(String code) {
        this.code = code.trim().toUpperCase();
    }
}
