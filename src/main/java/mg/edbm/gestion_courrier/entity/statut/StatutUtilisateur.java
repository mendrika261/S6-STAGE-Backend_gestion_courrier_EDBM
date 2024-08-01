package mg.edbm.gestion_courrier.entity.statut;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatutUtilisateur {
    ACTIF(0),
    DESACTIVE(-10),
    SUPPRIME(-20);

    private final Integer code;
}
