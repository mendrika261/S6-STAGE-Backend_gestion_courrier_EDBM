package mg.edbm.mail.entity.type;

import lombok.Data;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.entity.User;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

@Data
public class Token {
    private String ipAddress;
    private String userAgent;
    private User user;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiredAt;
    private String value;

    public Token(String ipAddress, String userAgent, User user) {
        setIpAddress(ipAddress);
        setUserAgent(userAgent);
        setUser(user);
        setExpiredAt(getCreatedAt().plus(Duration.ofMillis(SecurityConfig.TOKEN_EXPIRATION_TIME)));
        generateValue();
    }

    public void generateValue() {
        final SecureRandom random = new SecureRandom();
        final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        final byte[] bytes = new byte[SecurityConfig.TOKEN_SIZE];
        random.nextBytes(bytes);
        setValue(base64Encoder.encodeToString(bytes));
    }
}
