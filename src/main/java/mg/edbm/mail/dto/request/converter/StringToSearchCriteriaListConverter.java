package mg.edbm.mail.dto.request.converter;

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
public class StringToSearchCriteriaListConverter implements Converter<String, List<SearchCriteria>> {
    @Override
    public List<SearchCriteria> convert(@NonNull String source) { // "AND:lastName:EQUAL:rakoto,OR:firstName:EQUAL:toto"

        final List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        final String[] searchCriteria = source.split(",");
        for (String criteria : searchCriteria) {
            final int logicOperatorIndex = 0;
            final int keyIndex = 1;
            final int operationIndex = 2;
            final int contentIndex = 3;
            final String[] split = criteria.split(":");
            final String content = split.length > contentIndex ? URLDecoder.decode(split[contentIndex], StandardCharsets.UTF_8) : "";
            final LogicOperationType logicOperationType = LogicOperationType.valueOf(split[logicOperatorIndex].toUpperCase());
            final OperationType operationType = OperationType.valueOf(split[operationIndex].toUpperCase());
            searchCriteriaList.add(new SearchCriteria(logicOperationType, split[keyIndex], operationType, content));
        }
        return searchCriteriaList;
    }
}
