package mg.edbm.gestion_courrier.entity.statut;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatutSession {
    TENTATIVE(-10),
    ACTIVE(0),
    FINIE(10);

    private final Integer code;
}
