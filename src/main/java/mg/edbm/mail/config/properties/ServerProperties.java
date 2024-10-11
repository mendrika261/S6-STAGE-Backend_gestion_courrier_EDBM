package mg.edbm.mail.config.properties;

import jakarta.annotation.PostConstruct;
import mg.edbm.mail.exception.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ServerProperties {
    @Value("${mg.edbm.mail.config.allowed-origins}")
    public String[] ALLOWED_ORIGINS;
    @Value("${mg.edbm.mail.config.allowed-headers}")
    public String[] ALLOWED_HEADERS;
    @Value("${mg.edbm.mail.config.allowed-methods}")
    public String[] ALLOWED_METHODS;

    @PostConstruct
    public void validateProperties() {
        if (ALLOWED_ORIGINS == null || ALLOWED_ORIGINS.length == 0) {
            throw new ConfigurationException("Allowed origins must be defined " +
                    "eg. http://localhost:8080,https://example.com");
        }
        if (ALLOWED_HEADERS == null || ALLOWED_HEADERS.length == 0) {
            throw new ConfigurationException("Allowed headers must be defined" +
                    "eg. Authorization,Content-Type");
        }
        if (ALLOWED_METHODS == null || ALLOWED_METHODS.length == 0) {
            throw new ConfigurationException("Allowed methods must be defined" +
                    "eg. GET,POST,PUT,DELETE,OPTIONS");
        }
    }

    private boolean isValidUrl(String string) {
        return string.matches("^(http|https)://.*$");
    }
}
