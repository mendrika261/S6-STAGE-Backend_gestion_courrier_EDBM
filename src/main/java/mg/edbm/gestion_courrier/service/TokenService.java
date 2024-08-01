package mg.edbm.gestion_courrier.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mg.edbm.gestion_courrier.entity.Session;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.entity.statut.StatutSession;
import mg.edbm.gestion_courrier.exception.AuthentificationException;
import mg.edbm.gestion_courrier.utils.Token;
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
    public final String JETON_TYPE = "Bearer";
    private final SessionService sessionService;
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public Token genererToken(HttpServletRequest request, Utilisateur utilisateur) {
        final Token token = new Token(request.getRemoteAddr(), request.getHeader("User-Agent"), utilisateur);
        final Session session = sessionService.getSession(utilisateur, request);
        if(session == null || session.getValeurToken() == null) {
            sessionService.creerSession(token);
        } else {
            token.setValeur(session.getValeurToken());
            sessionService.actualiserSession(session, token);
        }
        return token;
    }

    public String extractToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith(JETON_TYPE)) {
            final String token = authorizationHeader.substring(7);
            if (!token.isEmpty()) {
                return token;
            }
        }
        return null;
    }

    public void authentifierUtilisateur(HttpServletRequest request) throws AuthentificationException {
        final String valeurToken = extractToken(request);
        final Session session = sessionService.getSession(valeurToken);
        final Token token;

        try {
            token = verifierToken(session, request);
        } catch (AuthentificationException silenced) {
            logger.error(silenced.getMessage());
            return;
        }

        final Collection<SimpleGrantedAuthority> authorities = session.getCreationPar().getAuthorities();
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(session.getCreationPar(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        sessionService.actualiserSession(session, token);
    }

    public Token verifierToken(Session session, HttpServletRequest request) throws AuthentificationException {
        if (session == null) {
            throw new AuthentificationException("Token d'authentification invalide: " + request.getHeader("Authorization"));
        }
        if (!session.getAdresseIp().equals(request.getRemoteAddr())) {
            throw new AuthentificationException("Adresse IP non autorisée: " + request.getRemoteAddr());
        }
        if (!session.getUserAgent().equals(request.getHeader("User-Agent"))) {
            throw new AuthentificationException("Utilisateur du token non autorisé: " + request.getHeader("User-Agent"));
        }
        if (!session.getStatut().equals(StatutSession.ACTIVE)) {
            throw new AuthentificationException("Token d'authentification inactif: " + request.getHeader("Authorization"));
        }
        if (session.getDateHeureExpiration().isBefore(LocalDateTime.now())) {
            session.setStatut(StatutSession.FINIE);
            throw new AuthentificationException("Token d'authentification expiré: " + request.getHeader("Authorization"));
        }

        return new Token(
                session.getAdresseIp(),
                session.getUserAgent(),
                session.getCreationPar()
        );
    }
}
