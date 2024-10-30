package mg.edbm.mail.dto.request.converter;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StringToLongListConverter implements Converter<String, List<Long>> {
    @Override
    public List<Long> convert(@NonNull String source) {
        final JsonParser jsonParser = JsonParserFactory.getJsonParser();
        final List<Object> objects = jsonParser.parseList(source);
        return objects.stream().map(o -> Long.parseLong((String) o)).toList();
    }
}