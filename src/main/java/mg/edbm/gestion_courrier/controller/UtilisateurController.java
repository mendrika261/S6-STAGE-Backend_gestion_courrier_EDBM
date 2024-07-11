package mg.edbm.gestion_courrier.controller;

import lombok.Getter;
import mg.edbm.gestion_courrier.service.UtilisateurService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/auth")
    public String authentifier(String email, String motDePasse) {
        return utilisateurService.authentifier(email, motDePasse);
    }

    @Secured("ADMIN")
    @GetMapping("/ok")
    public String ok() {
        return "Authentification réussie";
    }
}
