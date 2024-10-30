package mg.edbm.mail.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.edbm.mail.entity.Location;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class LocationDto {
    private Long id;
    @NotBlank(message = "Le nom de la localisation est obligatoire")
    @Length(min = 1, max = 255, message = "Le nom de la localisation doit être compris entre 1 et 255 caractères")
    private String name;
    @NotBlank(message = "La latitude est obligatoire")
    private Double latitude;
    @NotBlank(message = "La longitude est obligatoire")
    private Double longitude;

    public LocationDto(Location location) {
        setId(location.getId());
        setName(location.getName());
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
    }

    public void setName(String name) {
        this.name = name.trim().toLowerCase();
    }
}
