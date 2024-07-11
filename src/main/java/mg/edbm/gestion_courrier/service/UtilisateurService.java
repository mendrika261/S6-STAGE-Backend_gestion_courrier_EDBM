package mg.edbm.gestion_courrier.service;

import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.repository.UtilisateurRepository;
import mg.edbm.gestion_courrier.security.JwtService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              BCryptPasswordEncoder passwordEncoder,
                              JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Utilisateur verifierUtilisateur(String email, String motDePasse) {
        final Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        if(utilisateur.isPresent()) {
            final Utilisateur utilisateurActuel = utilisateur.get();
            if(passwordEncoder.matches(motDePasse, utilisateurActuel.getMotDePasse()) && utilisateurActuel.estValide()) {
                return utilisateurActuel;
            } else {
                throw new UsernameNotFoundException("Mot de passe incorrect");
            }
        }
        throw new UsernameNotFoundException("Utilisateur non trouvé");
    }

    public String authentifier(String email, String motDePasse) {
        final Utilisateur utilisateur = verifierUtilisateur(email, motDePasse);
        return jwtService.nouveauToken(utilisateur);
    }
}
