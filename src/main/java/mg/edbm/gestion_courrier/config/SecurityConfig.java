package mg.edbm.gestion_courrier.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import mg.edbm.gestion_courrier.dto.Raison;
import mg.edbm.gestion_courrier.dto.Reponse;
import mg.edbm.gestion_courrier.filter.TokenRequestFilter;
import mg.edbm.gestion_courrier.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(securedEnabled = true)
@Getter
public class SecurityConfig {
    public static final String ROLE_ADMIN = "ADMIN";

    public void handleAuthentificationException(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Exception authException) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Reponse<Raison> raison = new Reponse<>("Authentification requise", null);
        response.setStatus(Reponse.NON_AUTHENTIFIE);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("WWW-Authenticate", "Bearer (token-ici)");
        response.getWriter().write(objectMapper.writeValueAsString(raison));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenService tokenService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new TokenRequestFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/auth", "/error").permitAll();
                            auth.anyRequest().authenticated();
                        })
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(this::handleAuthentificationException)
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
