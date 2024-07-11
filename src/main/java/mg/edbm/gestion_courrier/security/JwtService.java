package mg.edbm.gestion_courrier.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final AeadAlgorithm ENC = Jwts.ENC.A256GCM;
    private final SecretKey CLE_SECRET = ENC.key().build();
    private final long VALIDITE_TOKEN = 30 * 60 * 1000; // 30 minutes

    public String nouveauToken(Utilisateur utilisateur) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("roles", utilisateur.getRolesCode());
        final Date dateActuelle = new Date(System.currentTimeMillis());
        final Date dateExpiration = new Date(System.currentTimeMillis() + VALIDITE_TOKEN);

        return Jwts.builder()
                .claims(claims)
                .subject(utilisateur.getId().toString())
                .issuedAt(dateActuelle)
                .expiration(dateExpiration)
                .encryptWith(CLE_SECRET, ENC)
                .compact();
    }

    public void authentifierUtilisateur(String authorizationHeader) {
        final Claims claims = extractClaims(authorizationHeader);
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

    private Claims extractClaims(String authorizationHeader) {
        final String jwtString = extractJwtString(authorizationHeader);
        if(jwtString != null) {
            try {
                final Claims claims = Jwts.parser()
                        .decryptWith(CLE_SECRET)
                        .build()
                        .parseEncryptedClaims(jwtString)
                        .getPayload();
                return valideClaims(claims);
            } catch (Exception ignored) {}
        }
        return null;
    }

    public Claims valideClaims(Claims claims) {
        if (claims == null || claims.getExpiration() == null || claims.getSubject() == null) {
            throw new RuntimeException("Token invalide");
        }
        if (tokenEstExpire(claims)) {
            throw new RuntimeException("Token expiré");
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
