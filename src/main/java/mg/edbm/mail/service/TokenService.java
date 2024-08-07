package mg.edbm.mail.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.properties.TokenProperties;
import mg.edbm.mail.entity.Session;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.SessionStatus;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.entity.type.Token;
import mg.edbm.mail.exception.type.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private final TokenProperties tokenProperties;
    private final SessionService sessionService;

    public Token generateToken(HttpServletRequest request, User user) {
        final Token token = new Token(
                request.getRemoteAddr(), request.getHeader("User-Agent"), user,
                tokenProperties.TOKEN_DURATION_MINUTES, tokenProperties.TOKEN_LENGTH
        );
        final Optional<Session> sessionOptional = sessionService.getExistingActiveSession(user, request);
        if(sessionOptional.isPresent()) {
            final Session session = sessionOptional.get();
            token.setValue(session.getTokenValue());
            sessionService.extend(session, token);
        } else {
            sessionService.create(token);
        }
        return token;
    }

    public String extractToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith(tokenProperties.TOKEN_TYPE)) {
            final String token = authorizationHeader.substring(7);
            if (!token.isEmpty()) {
                return token;
            }
        }
        return null;
    }

    @Transactional
    public void authenticateUser(HttpServletRequest request) {
        final String tokenValue = extractToken(request);
        final Session session = sessionService.getSession(tokenValue);
        try {
            final Token token = validateToken(session, request);

            final Collection<SimpleGrantedAuthority> authorities = session.getUser().getAuthorities();
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(session.getUser(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            sessionService.extend(session, token);
        } catch (AuthenticationException ignored) {}
    }

    public Token validateToken(Session session, HttpServletRequest request) throws AuthenticationException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String ipAddress = request.getRemoteAddr();
        final String userAgent = request.getHeader("User-Agent");
        if (session == null) {
            throw new AuthenticationException(
                    "Token does not exist or in invalid format [%s]".formatted(authorizationHeader),
                    LogType.WARN
            );
        }
        if (session.getExpiredAt().isBefore(LocalDateTime.now())) {
            session.setStatus(SessionStatus.EXPIRED);
            throw new AuthenticationException("Token expired: " + session, LogType.WARN);
        }
        if(!session.getStatus().equals(SessionStatus.ACTIVE)) {
            throw new AuthenticationException("Token revoked: " + session, LogType.ERROR);
        }
        if (!session.getIpAddress().equals(ipAddress) || !session.getUserAgent().equals(userAgent)) {
            final Session intrusionSession = sessionService.createIntrusionSession(session, request);
            throw new AuthenticationException("Token intrusion: " + intrusionSession);
        }

        return new Token(
                session.getIpAddress(), session.getUserAgent(), session.getUser(),
                tokenProperties.TOKEN_DURATION_MINUTES, tokenProperties.TOKEN_LENGTH
        );
    }
}
