package mg.edbm.mail.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.mail.dto.request.type.LogicOperationType;
import mg.edbm.mail.dto.request.type.OperationType;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private LogicOperationType logicOperationType = LogicOperationType.AND;
    private String key;
    private OperationType operation;
    private Object value;

    public SearchCriteria(String key, OperationType operation, Object value) {
        setKey(key);
        setOperation(operation);
        setValue(value);
    }
}
