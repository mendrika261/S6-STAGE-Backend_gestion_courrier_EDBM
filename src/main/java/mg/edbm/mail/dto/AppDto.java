package mg.edbm.mail.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.edbm.mail.entity.App;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDto {
    private UUID id;
    @Length(min = 1, max = 255, message = "Le nom de l'application doit être compris entre 1 et 255 caractères")
    @NotBlank(message = "Le nom de l'application est obligatoire")
    private String name;
    @Length(min = 1, max = 255, message = "La description de l'application doit être comprise entre 1 et 255 caractères")
    private String description;
    @Length(min = 1, max = 255, message = "L'URL du logo de l'application doit être comprise entre 1 et 255 caractères")
    @URL(protocol = "http", message = "L'URL du logo doit être sous la forme http://...")
    private String logoUrl;
    @Length(min = 1, max = 255, message = "L'URL de l'application doit être comprise entre 1 et 255 caractères")
    @NotBlank(message = "L'URL de l'application est obligatoire")
    @URL(protocol = "http", message = "L'URL de l'application doit être sous la forme http://...")
    private String url;
    @Length(min = 1, max = 255, message = "Le préfixe des rôles autorisés de l'application doit être compris entre 1 et 255 caractères")
    @NotBlank(message = "Le préfixe des rôles autorisés de l'application est obligatoire")
    private String authorizedRolePrefix;
    private LocalDateTime createdAt;

    public AppDto(App app) {
        setId(app.getId());
        setName(app.getName());
        setDescription(app.getDescription());
        setLogoUrl(app.getLogoUrl());
        setUrl(app.getUrl());
        setAuthorizedRolePrefix(app.getAuthorizedRolePrefix());
        setCreatedAt(app.getCreatedAt());
    }

    public static List<AppDto> from(List<App> apps) {
        return apps.stream()
                .map(AppDto::new)
                .toList();
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setDescription(String description) {
        if(description != null)
            this.description = description.trim();
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl.toLowerCase().trim();
    }

    public void setUrl(String url) {
        this.url = url.toLowerCase().trim();
    }

    public void setAuthorizedRolePrefix(String authorizedRolePrefix) {
        this.authorizedRolePrefix = authorizedRolePrefix.toUpperCase().trim();
    }
}
