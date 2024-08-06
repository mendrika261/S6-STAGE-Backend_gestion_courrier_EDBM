package mg.edbm.mail;

import mg.edbm.mail.repository.RoleRepository;
import mg.edbm.mail.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class GestionCourrierApplicationTests {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

	@Test
	void contextLoads() {

		/*User utilisateur = new User();
		utilisateur.setNom("Rakoto");
		utilisateur.setPrenom("Toto");
		utilisateur.setEmail("toto@rakoto.com");
		utilisateur.setMotDePasse(passwordEncoder.encode("test"));
		utilisateur.setPhoneNumber("0321093828");
		utilisateurRepository.saveAndFlush(utilisateur);

		Role role = new Role();
		role.setNom("Administrateur");
		role.setCode("ADMIN");
		roleRepository.saveAndFlush(role);*/

		//System.out.println(passwordEncoder.encode("test"));
	}

}
