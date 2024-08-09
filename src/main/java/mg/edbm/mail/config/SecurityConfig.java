package mg.edbm.mail.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.config.properties.ServerProperties;
import mg.edbm.mail.dto.response.MessageResponse;
import mg.edbm.mail.filter.TokenRequestFilter;
import mg.edbm.mail.repository.UserRepository;
import mg.edbm.mail.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_RECEPTIONIST = "RECEPTIONIST";
    public static final String ROLE_MESSENGER = "MESSENGER";
    public static final String ROLE_USER = "USER";

    private final UserRepository userRepository;
    private final ServerProperties serverProperties;

    public static void handleAuthentificationException(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Exception authException) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final MessageResponse messageResponse = new MessageResponse("Authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(messageResponse));
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(serverProperties.ALLOWED_ORIGINS));
        configuration.setAllowedMethods(Arrays.asList(serverProperties.ALLOWED_METHODS));
        configuration.setAllowedHeaders(Arrays.asList(serverProperties.ALLOWED_HEADERS));

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
                                .authenticationEntryPoint(SecurityConfig::handleAuthentificationException)
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
