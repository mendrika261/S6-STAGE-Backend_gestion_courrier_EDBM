package mg.edbm.gestion_courrier.service;

import jakarta.servlet.http.HttpServletRequest;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.exception.AuthentificationException;
import mg.edbm.gestion_courrier.repository.UtilisateurRepository;
import mg.edbm.gestion_courrier.dto.response.TokenResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              BCryptPasswordEncoder passwordEncoder,
                              TokenService tokenService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public Utilisateur verifierUtilisateur(String email, String motDePasse) throws AuthentificationException {
        final Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        if(utilisateur.isPresent()) {
            final Utilisateur utilisateurActuel = utilisateur.get();
            if(passwordEncoder.matches(motDePasse, utilisateurActuel.getMotDePasse()) && utilisateurActuel.estValide()) {
                return utilisateurActuel;
            } else {
                throw new AuthentificationException("Mot de passe incorrect");
            }
        }
        throw new AuthentificationException("Utilisateur non trouvé");
    }

    public TokenResponse authentifierAvecMotDePasseOuJeton(String email, String motDePasse, HttpServletRequest request)
            throws AuthentificationException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Utilisateur utilisateur;

        // Authentification avec jeton
        if(authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != null
                && !authentication.getPrincipal().toString().equals("anonymousUser")) {
            utilisateur = utilisateurRepository.findById(UUID.fromString(authentication.getPrincipal().toString()))
                    .orElseThrow(() -> new AuthentificationException("Authentification avec mot de passe nécessaire"));
        } else { // authentification avec email et mot de passe
            utilisateur = verifierUtilisateur(email, motDePasse);
        }
        return tokenService.nouveauToken(utilisateur, request);
    }
}
