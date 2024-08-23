package mg.edbm.mail;

import mg.edbm.mail.exception.ValidationException;
import mg.edbm.mail.repository.RoleRepository;
import mg.edbm.mail.repository.UserRepository;
import mg.edbm.mail.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@SpringBootTest
class GestionCourrierApplicationTests {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;

    @Test
	void contextLoads() throws ValidationException {

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

        /*final Context context = new Context();
        emailService.sendEmail("harenasitrakiniavo10@gmail.com", "Test", "new-user-email", context);*/

        System.out.println(LocalDateTime.parse("2024-08-22T23:28:29.548603"));
	}

}
