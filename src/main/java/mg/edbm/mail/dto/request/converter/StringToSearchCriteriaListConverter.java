package mg.edbm.mail.dto.request.converter;

import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.type.OperationType;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StringToSearchCriteriaListConverter implements Converter<String, List<SearchCriteria>> {
    @Override
    public List<SearchCriteria> convert(@NonNull String source) {
        final JsonParser parser = JsonParserFactory.getJsonParser();
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        List<Object> parsedList = parser.parseList(source);
        for (Object object : parsedList) {
            if(object instanceof Map) {
                Map<String, Object> parsedMap = (Map<String, Object>) object;
                String key = (String) parsedMap.get("key");
                OperationType operation = OperationType.valueOf((String) parsedMap.get("operation"));
                Object value = parsedMap.get("value");
                searchCriteriaList.add(new SearchCriteria(key, operation, value));
            }
        }
        return searchCriteriaList;
    }
}
