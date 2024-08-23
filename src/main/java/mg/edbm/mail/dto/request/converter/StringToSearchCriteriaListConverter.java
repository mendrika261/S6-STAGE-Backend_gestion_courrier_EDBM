package mg.edbm.mail.dto.request.converter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.type.LogicOperationType;
import mg.edbm.mail.dto.request.type.OperationType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
@Getter
@NoArgsConstructor
public class StringToSearchCriteriaListConverter implements Converter<String, List<SearchCriteria>> {
    private final String CRITERIA_SEPARATOR = ",";
    private final String CRITERIA_PART_SEPARATOR = ":";
    private final int logicOperatorIndex = 0;
    private final int keyIndex = 1;
    private final int operationIndex = 2;
    private final int contentIndex = 3;

    private List<SearchCriteria> getSearchCriteria(String[] split) {
        final List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        final LogicOperationType logicOperationType = LogicOperationType.valueOf(split[logicOperatorIndex].toUpperCase());
        final String field = split[keyIndex];
        final OperationType operationType = OperationType.valueOf(split[operationIndex].toUpperCase());
        final String content = split.length > contentIndex ? URLDecoder.decode(split[contentIndex], StandardCharsets.UTF_8) : "";
        if(operationType.equals(OperationType.LIKE) || operationType.equals(OperationType.NOT_LIKE)) {
            final String[] words = content.split(" ");
            for (String word : words)
                searchCriteriaList.add(new SearchCriteria(logicOperationType, field, operationType, word));
        } else searchCriteriaList.add(new SearchCriteria(logicOperationType, field, operationType, content));
        return searchCriteriaList;
    }

    @Override
    public List<SearchCriteria> convert(@NonNull String source) { // "AND:lastName:EQUAL:rakoto,OR:firstName:EQUAL:toto"
        final List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        final String[] searchCriteria = source.split(CRITERIA_SEPARATOR);

        for (String searchCriterion : searchCriteria) {
            final String[] split = searchCriterion.split(CRITERIA_PART_SEPARATOR);
            searchCriteriaList.addAll(getSearchCriteria(split));
        }
        searchCriteriaList.get(0).setLogicOperationType(LogicOperationType.AND);
        // log.info("{}", searchCriteriaList);
        return searchCriteriaList;
    }
}
