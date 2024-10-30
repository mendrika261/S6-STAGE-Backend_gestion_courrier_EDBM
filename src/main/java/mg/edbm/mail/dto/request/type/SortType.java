package mg.edbm.mail.dto.request.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {
    ASC("asc"),
    DESC("desc");

    private final String valeur;
}
