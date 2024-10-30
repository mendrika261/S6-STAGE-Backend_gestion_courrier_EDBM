package mg.edbm.mail.dto.request.converter;

import mg.edbm.mail.dto.request.type.SortType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class StringToOrderConverter implements Converter<String, HashMap<String, SortType>> {
    @Override
    public HashMap<String, SortType> convert(@NonNull String source) { //code:asc,name:desc
        final HashMap<String, SortType> map = new HashMap<>();
        final String[] orders = source.split(",");
        for (String order : orders) {
            final String[] split = order.split(":");
            map.put(split[0], SortType.valueOf(split[1].toUpperCase()));
        }
        return map;
    }
}