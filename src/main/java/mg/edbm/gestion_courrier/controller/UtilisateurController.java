package mg.edbm.gestion_courrier.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import mg.edbm.gestion_courrier.config.SecurityConfig;
import mg.edbm.gestion_courrier.dto.request.AuthentificationRequest;
import mg.edbm.gestion_courrier.dto.response.Reponse;
import mg.edbm.gestion_courrier.dto.response.TokenResponse;
import mg.edbm.gestion_courrier.exception.AuthentificationException;
import mg.edbm.gestion_courrier.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/auth")
    public ResponseEntity<Reponse<TokenResponse>> authentifier(@Valid AuthentificationRequest authentificationRequest,
                                                               HttpServletRequest request) throws AuthentificationException {
        return Reponse.envoyer(utilisateurService.authentifierAvecMotDePasseOuJeton(authentificationRequest.getEmail(),
                authentificationRequest.getMotDePasse(), request));
    }

    @Secured(SecurityConfig.ROLE_ADMIN)
    @GetMapping("/ok")
    public String ok() {
        return "Authentification réussie";
    }
}
