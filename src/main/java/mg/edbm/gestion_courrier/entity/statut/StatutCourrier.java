package mg.edbm.gestion_courrier.entity.statut;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatutCourrier {
    EN_ATTENTE(0),
    EN_COURS(10),
    TERMINE(20),
    ANNULE(30);

    private final Integer code;
}
