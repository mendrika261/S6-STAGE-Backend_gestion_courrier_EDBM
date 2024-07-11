package mg.edbm.gestion_courrier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Token {
    private String valeur;
    private Date dateExpiration;
}
