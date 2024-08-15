package mg.edbm.mail.dto.request.converter;

import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.type.OperationType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class StringToSearchCriteriaListConverter implements Converter<String, List<SearchCriteria>> {
    @Override
    public List<SearchCriteria> convert(@NonNull String source) {

        final List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        final String[] searchCriteria = source.split(",");
        for (String criteria : searchCriteria) {
            final String[] split = criteria.split(":");
            final String content = split.length > 2 ? URLDecoder.decode(split[2], StandardCharsets.UTF_8) : "";
            searchCriteriaList.add(new SearchCriteria(
                    split[0],
                    OperationType.valueOf(split[1].toUpperCase()),
                    content
            ));
        }
        return searchCriteriaList;
    }
}
