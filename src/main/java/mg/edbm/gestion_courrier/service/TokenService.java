package mg.edbm.gestion_courrier.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import mg.edbm.gestion_courrier.dto.Token;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.exception.AuthentificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private final AeadAlgorithm ENC = Jwts.ENC.A256GCM;
    private final SecretKey CLE_SECRET = ENC.key().build();
    private final long VALIDITE_TOKEN = 30 * 60 * 1000; // 30 minutes

    public Token nouveauToken(Utilisateur utilisateur, HttpServletRequest request) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("roles", utilisateur.getRolesCode());
        claims.put("ip", request.getRemoteAddr());
        claims.put("userAgent", request.getHeader("User-Agent"));

        final Date dateActuelle = new Date(System.currentTimeMillis());
        final Date dateExpiration = new Date(System.currentTimeMillis() + VALIDITE_TOKEN);

        return new Token(Jwts.builder()
                    .claims(claims)
                    .subject(utilisateur.getId().toString())
                    .issuedAt(dateActuelle)
                    .expiration(dateExpiration)
                    .encryptWith(CLE_SECRET, ENC)
                    .compact(),
                dateExpiration);
    }

    public void authentifierUtilisateur(HttpServletRequest request) {
        //final String authorizationHeader = request.getHeader("Authorization");
        final Claims claims = extractClaims(request);
        if(claims == null) return;

        final Collection<SimpleGrantedAuthority> authorities = extractRoles(claims);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    public String extractJwtString(String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String jwtString = authorizationHeader.substring(7);
            if (!jwtString.isEmpty()) {
                return jwtString;
            }
        }
        return null;
    }

    private Claims extractClaims(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwtString = extractJwtString(authorizationHeader);
        if(jwtString != null) {
            try {
                final Claims claims = Jwts.parser()
                        .decryptWith(CLE_SECRET)
                        .build()
                        .parseEncryptedClaims(jwtString)
                        .getPayload();
                return valideClaims(claims, request);
            } catch (Exception ignored) {}
        }
        return null;
    }

    public Claims valideClaims(Claims claims, HttpServletRequest request) throws AuthentificationException {
        if (claims == null || claims.getExpiration() == null || claims.getSubject() == null) {
            throw new AuthentificationException("Jeton d'authentification invalide");
        }
        if(claims.get("roles", List.class) == null) {
            throw new AuthentificationException("Jeton d'authentification sans rôles");
        }
        if (!claims.get("ip", String.class).equals(request.getRemoteAddr())) {
            throw new AuthentificationException("Adresse IP non autorisée");
        }
        if (!claims.get("userAgent", String.class).equals(request.getHeader("User-Agent"))) {
            throw new AuthentificationException("Utilisateur du jeton non autorisé");
        }
        if (tokenEstExpire(claims)) {
            throw new AuthentificationException("Jeton d'authentification expiré");
        }
        return claims;
    }

    private Boolean tokenEstExpire(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private Collection<SimpleGrantedAuthority> extractRoles(Claims claims) {
        return Arrays.stream((Object[]) claims.get("roles", List.class).toArray())
                .map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
