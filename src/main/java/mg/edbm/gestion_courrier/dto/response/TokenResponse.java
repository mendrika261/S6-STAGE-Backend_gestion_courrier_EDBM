package mg.edbm.gestion_courrier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.gestion_courrier.utils.Token;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TokenResponse {
    private UtilisateurResponse utilisateur;
    private String token;
    private LocalDateTime dateHeureExpiration;

    public TokenResponse(Token token) {
        setUtilisateur(new UtilisateurResponse(token.getUtilisateur()));
        setToken(token.getValeur());
        setDateHeureExpiration(token.getDateHeureExpiration());
    }
}
