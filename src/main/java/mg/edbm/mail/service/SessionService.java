package mg.edbm.mail.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.entity.Session;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.SessionStatus;
import mg.edbm.mail.repository.SessionRepository;
import mg.edbm.mail.entity.type.Token;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public void save(Session session) {
        sessionRepository.save(session);
    }

    public void create(Token token) {
        final Session session = new Session(
                token.getValue(),
                SessionStatus.WORKING,
                token.getUserAgent(),
                token.getIpAddress(),
                token.getUser(),
                token.getExpiredAt()
        );
        save(session);
    }

    public void extend(Session session, Token token) {
        session.setStatus(SessionStatus.WORKING);
        session.setLastActivityAt(LocalDateTime.now());
        session.setExpiredAt(token.getExpiredAt());
        save(session);
    }

    public Session getSession(String tokenValue) {
        return sessionRepository.findByTokenValue(tokenValue);
    }

    public Optional<Session> getExistingWorkingSession(User user, HttpServletRequest request) {
        return sessionRepository.findExistingSession(
                user,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                SessionStatus.WORKING
        );
    }

    public Optional<Session> getExistingIntrusionSession(User user, HttpServletRequest request) {
        return sessionRepository.findExistingSession(
                user,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                SessionStatus.INTRUSION
        );
    }

    public void createIntrusionSession(Session session, HttpServletRequest request) {
        final Optional<Session> intrusionSessionOptional = getExistingIntrusionSession(session.getUser(), request);
        if(intrusionSessionOptional.isPresent()) {
            final Session existingIntrusionSession = intrusionSessionOptional.get();
            existingIntrusionSession.setLastActivityAt(LocalDateTime.now());
            save(existingIntrusionSession);
        } else {
            final Session intrusionSession = new Session(
                    session.getTokenValue(),
                    SessionStatus.INTRUSION,
                    request.getHeader("User-Agent"),
                    request.getRemoteAddr(),
                    session.getUser(),
                    session.getExpiredAt()
            );
            save(intrusionSession);
        }
    }
}
