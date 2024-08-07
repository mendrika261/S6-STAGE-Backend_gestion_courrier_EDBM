package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.entity.type.SessionStatus;
import mg.edbm.mail.entity.type.Token;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "session")
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(unique = true)
    private String tokenValue;

    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime lastActivityAt = LocalDateTime.now();

    @Column(nullable = false)
    private String userAgent;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private Long queryCount = 1L;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Session(String tokenValue, SessionStatus status, String userAgent, String ipAddress,
                   User user, LocalDateTime expiredAt) {
        setTokenValue(tokenValue);
        setStatus(status);
        setUserAgent(userAgent);
        setIpAddress(ipAddress);
        setUser(user);
        setExpiredAt(expiredAt);
    }

    public void extend(Token token) {
        setStatus(SessionStatus.ACTIVE);
        setExpiredAt(token.getExpiredAt());
        refresh();
    }

    public void refresh() {
        setLastActivityAt(LocalDateTime.now());
        incrementQueryCount();
    }

    public void incrementQueryCount() {
        queryCount++;
    }

    @Override
    public String toString() {
        return "(%s) [%s] for user: %s using %s at %s, last activity at: %s and expired at: %s".formatted(
                getTokenValue(), getStatus(), getUser().getUsername(), getIpAddress(),
                getUserAgent(), getLastActivityAt(), getExpiredAt()
        );
    }
}