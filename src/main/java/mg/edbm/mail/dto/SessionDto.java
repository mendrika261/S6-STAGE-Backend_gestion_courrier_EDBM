package mg.edbm.mail.dto;

import lombok.Data;
import mg.edbm.mail.dto.response.UserResponse;
import mg.edbm.mail.entity.Session;
import mg.edbm.mail.entity.type.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SessionDto {
    private UUID id;
    private LocalDateTime expiredAt;
    private SessionStatus status;
    private LocalDateTime lastActivityAt;
    private String userAgent;
    private String ipAddress;
    private Long queryCount;
    private UserResponse user;
    private LocalDateTime createdAt;

    public SessionDto(Session session) {
        setId(session.getId());
        setExpiredAt(session.getExpiredAt());
        setStatus(session.getStatus());
        setLastActivityAt(session.getLastActivityAt());
        setUserAgent(session.getUserAgent());
        setIpAddress(session.getIpAddress());
        setQueryCount(session.getQueryCount());
        setUser(new UserResponse(session.getUser()));
        setCreatedAt(session.getCreatedAt());
    }
}
