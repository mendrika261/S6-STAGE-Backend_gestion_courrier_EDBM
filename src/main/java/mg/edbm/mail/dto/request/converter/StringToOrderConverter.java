package mg.edbm.mail.dto.request.converter;

import mg.edbm.mail.dto.request.type.SortType;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class StringToOrderConverter implements Converter<String, HashMap<String, SortType>> {
    @Override
    public HashMap<String, SortType> convert(@NonNull String source) {
        final HashMap<String, SortType> map = new HashMap<>();
        final JsonParser parser = JsonParserFactory.getJsonParser();
        parser.parseMap(source).forEach((key, value) -> map.put(key, SortType.valueOf(value.toString().toUpperCase())));
        return map;
    }
}