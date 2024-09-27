package mg.edbm.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.mail.entity.App;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class AppDto {
    private String name;
    private String description;
    private String logoUrl;
    private String url;
    private String authorizedRolePrefix;
    private LocalDateTime createdAt;

    public AppDto(App app) {
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
}
