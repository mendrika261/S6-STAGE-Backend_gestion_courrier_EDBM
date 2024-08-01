package mg.edbm.gestion_courrier.dto.response;

import lombok.Data;
import mg.edbm.gestion_courrier.entity.Role;
import mg.edbm.gestion_courrier.entity.Utilisateur;

@Data
public class UtilisateurResponse {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String contact;
    private String[] roles;

    public UtilisateurResponse(Utilisateur utilisateur) {
        setId(utilisateur.getId().toString());
        setNom(utilisateur.getNom());
        setPrenom(utilisateur.getPrenom());
        setEmail(utilisateur.getEmail());
        setContact(utilisateur.getContact());
        setRoles(utilisateur.getRoles().stream().map(Role::getCode).toArray(String[]::new));
    }
}
