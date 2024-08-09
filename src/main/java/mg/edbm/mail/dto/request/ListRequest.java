package mg.edbm.mail.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.dto.request.type.SortType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ListRequest {
    @Min(message = "La page doit être supérieure ou égale à 1", value = 1)
    private Integer page = 1;
    @Min(message = "Le nombre d'élément doit être supérieur ou égal à 1", value = 1)
    private Integer limit = 10;
    private HashMap<String, SortType> orders = new HashMap<>();
    private List<SearchCriteria> criteria = new ArrayList<>();

    public Pageable toPageable() {
        return PageRequest.of(getPage() - 1, getLimit(), toSort());
    }

    public Sort toSort() {
        Sort sort = Sort.unsorted();
        for (String field : getOrders().keySet()) {
            final SortType direction = getOrders().get(field);

            if (direction == SortType.ASC)
                sort = sort.and(Sort.by(Sort.Order.asc(field)));
            else if (direction == SortType.DESC)
                sort = sort.and(Sort.by(Sort.Order.desc(field)));
        }
        return sort;
    }

    public void addOrder(String field, SortType direction) {
        getOrders().put(field, direction);
    }

    public void addCriteria(SearchCriteria searchCriteria) {
        getCriteria().add(searchCriteria);
    }

    public void addCriteria(String key, OperationType operation, Object value) {
        addCriteria(new SearchCriteria(key, operation, value));
    }
}
