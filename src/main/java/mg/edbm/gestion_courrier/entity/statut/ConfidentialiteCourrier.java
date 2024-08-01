package mg.edbm.gestion_courrier.entity.statut;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfidentialiteCourrier {
    PUBLIC(0),
    PRIVE(10),
    CONFIDENTIEL(20);

    private final Integer code;
}
