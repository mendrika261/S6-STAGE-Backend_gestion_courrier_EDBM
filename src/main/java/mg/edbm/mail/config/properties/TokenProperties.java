package mg.edbm.mail.config.properties;

import jakarta.annotation.PostConstruct;
import mg.edbm.mail.exception.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProperties {
    @Value("${mg.edbm.mail.config.token.type}")
    public String TOKEN_TYPE;
    @Value("${mg.edbm.mail.config.token.duration-minutes}")
    public Long TOKEN_DURATION_MINUTES;
    @Value("${mg.edbm.mail.config.token.length}")
    public Integer TOKEN_LENGTH;

    @PostConstruct
    public void validateProperties() {
        if (TOKEN_TYPE == null || TOKEN_TYPE.isEmpty()) {
            throw new ConfigurationException("Token type must not be null or empty");
        }
        if (TOKEN_DURATION_MINUTES == null || TOKEN_DURATION_MINUTES <= 0) {
            throw new ConfigurationException("Token duration minutes must be a positive number");
        }
        if (TOKEN_LENGTH == null || TOKEN_LENGTH <= 0) {
            throw new ConfigurationException("Token length must be a positive number");
        }
    }
}
