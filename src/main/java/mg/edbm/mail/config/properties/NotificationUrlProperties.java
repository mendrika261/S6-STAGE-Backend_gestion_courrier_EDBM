package mg.edbm.mail.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class NotificationUrlProperties {
    @Value("${app.url}")
    private String APP_URL;
    @Value("${app.url.mail_preview}")
    public String MAIL_PREVIEW;
    @Value("${app.url.mail_transit}")
    public String MAIL_TRANSIT;
}
