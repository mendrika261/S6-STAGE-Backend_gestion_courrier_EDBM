package mg.edbm.mail.config;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.request.converter.StringToLongListConverter;
import mg.edbm.mail.dto.request.converter.StringToSearchCriteriaListConverter;
import mg.edbm.mail.dto.request.converter.StringToHashMapConverter;
import mg.edbm.mail.dto.request.converter.StringToOrderConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class WebConfig implements WebMvcConfigurer {
    private final StringToHashMapConverter stringToHashMapConverter;
    private final StringToOrderConverter stringToOrderConverter;
    private final StringToSearchCriteriaListConverter stringToSearchCriteriaListConverter;
    private final StringToLongListConverter stringToLongListConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToHashMapConverter);
        registry.addConverter(stringToOrderConverter);
        registry.addConverter(stringToSearchCriteriaListConverter);
        registry.addConverter(stringToLongListConverter);
    }
}
