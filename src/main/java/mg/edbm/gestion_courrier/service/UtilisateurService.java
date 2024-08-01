package mg.edbm.gestion_courrier.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.exception.AuthentificationException;
import mg.edbm.gestion_courrier.repository.UtilisateurRepository;
import mg.edbm.gestion_courrier.utils.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Utilisateur verifierUtilisateur(String email, String motDePasse) throws AuthentificationException {
        final Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        if(utilisateur.isPresent()) {
            final Utilisateur utilisateurActuel = utilisateur.get();
            final boolean motDePasseValide = passwordEncoder.matches(motDePasse, utilisateurActuel.getMotDePasse());
            if(utilisateurActuel.estValide() && motDePasseValide) {
                return utilisateurActuel;
            } else {
                throw new AuthentificationException("Votre mot de passe est incorrect");
            }
        }
        throw new AuthentificationException("Le compte n'existe pas encore ou inactif");
    }

    public Utilisateur getUtilisateurActif() {
        final String DEFAULT_ROLE_ANONYMOUS = "anonymousUser";
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final boolean estAuthentifie = authentication != null && authentication.isAuthenticated();
        if(estAuthentifie
                && authentication.getPrincipal() != null
                && !authentication.getPrincipal().toString().equals(DEFAULT_ROLE_ANONYMOUS)) {
            return (Utilisateur) authentication.getPrincipal();
        }
        return null;
    }

    @Transactional
    public Token authentifierAvecMotDePasse(String email, String motDePasse, HttpServletRequest request)
            throws AuthentificationException {
        final Utilisateur utilisateur = verifierUtilisateur(email, motDePasse);
        return tokenService.genererToken(request, utilisateur);
    }
}
