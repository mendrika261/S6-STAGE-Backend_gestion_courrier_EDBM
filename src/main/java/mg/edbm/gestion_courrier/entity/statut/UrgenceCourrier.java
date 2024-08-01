package mg.edbm.gestion_courrier.entity.statut;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UrgenceCourrier {
    NORMAL(0),
    URGENT(10),
    TRES_URGENT(20);

    private final Integer code;
}
