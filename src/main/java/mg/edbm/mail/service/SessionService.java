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

    public Session save(Session session) {
        return sessionRepository.save(session);
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
        session.extend(token);
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

    public Session createIntrusionSession(Session session, HttpServletRequest request) {
        final Optional<Session> intrusionSessionOptional = getExistingIntrusionSession(session.getUser(), request);
        if(intrusionSessionOptional.isPresent()) {
            final Session existingIntrusionSession = intrusionSessionOptional.get();
            existingIntrusionSession.setLastActivityAt(LocalDateTime.now());
            return save(existingIntrusionSession);
        }
        final Session intrusionSession = new Session(
                session.getTokenValue(),
                SessionStatus.INTRUSION,
                request.getHeader("User-Agent"),
                request.getRemoteAddr(),
                session.getUser(),
                session.getExpiredAt()
        );
        return save(intrusionSession);
    }

    public Optional<Session> getExistingTentativeSession(User user, HttpServletRequest request) {
        final Optional<Session> lastSessionOptional = sessionRepository.findLastSession(
                user,
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
        );
        if(lastSessionOptional.isPresent() && lastSessionOptional.get().getStatus() == SessionStatus.TENTATIVE)
            return lastSessionOptional;
        return Optional.empty();
    }

    public void createTentativeSession(User user, HttpServletRequest request) {
        final Optional<Session> tentativeSessionOptional = getExistingTentativeSession(user, request);
        if(tentativeSessionOptional.isPresent()) {
            final Session existingTentativeSession = tentativeSessionOptional.get();
            existingTentativeSession.refresh();
            save(existingTentativeSession);
            return;
        }
        final Session session = new Session(
                null,
                SessionStatus.TENTATIVE,
                request.getHeader("User-Agent"),
                request.getRemoteAddr(),
                user,
                LocalDateTime.now()
        );
        save(session);
    }
}
