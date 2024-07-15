package mg.edbm.gestion_courrier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String valeur;
    private Date dateExpiration;
}
