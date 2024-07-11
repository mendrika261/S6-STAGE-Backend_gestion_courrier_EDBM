package mg.edbm.gestion_courrier;

import mg.edbm.gestion_courrier.entity.Role;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.repository.RoleRepository;
import mg.edbm.gestion_courrier.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.UUID;

@SpringBootTest
class GestionCourrierApplicationTests {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private RoleRepository roleRepository;

	@Test
	void contextLoads() {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNom("Rakoto");
		utilisateur.setPrenom("Toto");
		utilisateur.setEmail("toto@rakoto.com");
		utilisateur.setMotDePasse(passwordEncoder.encode("test"));
		utilisateur.setContact("0321093828");
		utilisateurRepository.saveAndFlush(utilisateur);

		Role role = new Role();
		role.setNom("Administrateur");
		role.setCode("ADMIN");
		roleRepository.saveAndFlush(role);

	}

}
