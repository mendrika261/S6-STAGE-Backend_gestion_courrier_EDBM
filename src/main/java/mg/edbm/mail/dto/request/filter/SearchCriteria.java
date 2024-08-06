package mg.edbm.mail.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.mail.dto.request.type.OperationType;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private OperationType operation;
    private Object value;
}
