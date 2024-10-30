package mg.edbm.mail.dto.request.converter;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class StringToHashMapConverter implements Converter<String, HashMap<String, String>> {
    @Override
    public HashMap<String, String> convert(@NonNull String source) {
        final HashMap<String, String> map = new HashMap<>();
        final JsonParser parser = JsonParserFactory.getJsonParser();
        parser.parseMap(source).forEach((key, value) -> map.put(key, value.toString()));
        return map;
    }
}