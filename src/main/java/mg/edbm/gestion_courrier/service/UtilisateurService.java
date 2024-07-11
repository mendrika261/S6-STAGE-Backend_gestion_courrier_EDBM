package mg.edbm.gestion_courrier.service;

import jakarta.servlet.http.HttpServletRequest;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.exception.AuthentificationException;
import mg.edbm.gestion_courrier.repository.UtilisateurRepository;
import mg.edbm.gestion_courrier.dto.Token;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Token authentifier(String email, String motDePasse, HttpServletRequest request)
            throws AuthentificationException {
        final Utilisateur utilisateur = verifierUtilisateur(email, motDePasse);
        return tokenService.nouveauToken(utilisateur, request);
    }
}
