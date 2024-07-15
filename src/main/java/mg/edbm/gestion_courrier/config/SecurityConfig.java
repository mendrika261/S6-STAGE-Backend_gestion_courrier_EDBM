package mg.edbm.gestion_courrier.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import mg.edbm.gestion_courrier.dto.response.RaisonResponse;
import mg.edbm.gestion_courrier.dto.response.Reponse;
import mg.edbm.gestion_courrier.filter.TokenRequestFilter;
import mg.edbm.gestion_courrier.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(securedEnabled = true)
@Getter
@Setter
public class SecurityConfig {
    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;
    public static final String ROLE_ADMIN = "ADMIN";

    public void handleAuthentificationException(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Exception authException) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Reponse<RaisonResponse> raison = new Reponse<>("Authentification requise", null);
        response.setStatus(Reponse.NON_AUTHENTIFIE);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("WWW-Authenticate", "Bearer (token-ici)");
        response.getWriter().write(objectMapper.writeValueAsString(raison));
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(getAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // cookies ...

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenService tokenService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(
                        cors -> cors.configurationSource(corsConfigurationSource())
                )
                .addFilterBefore(new TokenRequestFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
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
