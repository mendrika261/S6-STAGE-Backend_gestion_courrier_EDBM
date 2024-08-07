package mg.edbm.mail.entity.type;

import lombok.Data;
import mg.edbm.mail.entity.User;

import java.security.SecureRandom;
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

    public Token(String ipAddress, String userAgent, User user, Long durationMinutes, Integer tokenLength) {
        setIpAddress(ipAddress);
        setUserAgent(userAgent);
        setUser(user);
        setExpiredAt(getCreatedAt().plusMinutes(durationMinutes));
        generateValue(tokenLength);
    }

    public void generateValue(Integer tokenLength) {
        final SecureRandom random = new SecureRandom();
        final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        final byte[] bytes = new byte[tokenLength];
        random.nextBytes(bytes);
        setValue(base64Encoder.encodeToString(bytes));
    }
}
