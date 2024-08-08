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

		/*User user = new User();
		user.setNom("Rakoto");
		user.setPrenom("Toto");
		user.setEmail("toto@rakoto.com");
		user.setMotDePasse(passwordEncoder.encode("test"));
		user.setPhoneNumber("0321093828");
		userRepository.saveAndFlush(user);

		Role role = new Role();
		role.setNom("Administrateur");
		role.setCode("ADMIN");
		roleRepository.saveAndFlush(role);*/

		//System.out.println(passwordEncoder.encode("test"));
	}

}
