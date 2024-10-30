package mg.edbm.mail;

import com.fasterxml.jackson.databind.JsonNode;
import mg.edbm.mail.exception.ValidationException;
import mg.edbm.mail.repository.RoleRepository;
import mg.edbm.mail.repository.UserRepository;
import mg.edbm.mail.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.context.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

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

    @Bean
    public WebClient mapService() {
        return WebClient.create("http://127.0.0.1:8082/ors/v2");
    }

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

        //http://localhost:8082/ors/v2/directions/driving-car?start=8.681495%2C49.41461&end=8.687872%2C49.420318

        JsonNode jsonNode = mapService().get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/directions/driving-car")
                                .queryParam("start", "47.52287238836289,-18.91063319067217")
                                .queryParam("end", "47.53076076507569,-18.905880346137934")
                                .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofSeconds(100));
        System.out.println(jsonNode.get("features").get(0).get("properties").get("segments").get(0).get("distance"));
        System.out.println(jsonNode.get("features").get(0).get("properties").get("segments").get(0).get("duration"));
        System.out.println(jsonNode.get("features").get(0).get("geometry").get("coordinates"));

        System.out.println(LocalDateTime.parse("2024-08-22T23:28:29.548603"));
	}

}
