package mg.edbm.gestion_courrier.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mg.edbm.gestion_courrier.entity.Session;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.entity.statut.StatutSession;
import mg.edbm.gestion_courrier.repository.SessionRepository;
import mg.edbm.gestion_courrier.utils.Token;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public void creerSession(Token token) {
        final Session session = new Session(
                token.getValeur(),
                StatutSession.ACTIVE,
                token.getUserAgent(),
                token.getAdresseIp(),
                token.getUtilisateur(),
                LocalDateTime.now(),
                token.getDateHeureExpiration()
        );
        sessionRepository.save(session);
    }

    public void actualiserSession(Session session, Token token) {
        session.setStatut(StatutSession.ACTIVE);
        session.setDateHeureDerniereActivite(LocalDateTime.now());
        session.setDateHeureExpiration(token.getDateHeureExpiration());
        sessionRepository.save(session);
    }

    public Session getSession(String valeurToken) {
        return sessionRepository.findByValeurToken(valeurToken);
    }

    public Session getSession(Utilisateur utilisateur, HttpServletRequest request) {
        return sessionRepository.findByCreationParAndAdresseIpAndUserAgentAndStatut(
                utilisateur,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                StatutSession.ACTIVE
        );
    }

}
