package mg.edbm.gestion_courrier.utils;

import lombok.Data;
import mg.edbm.gestion_courrier.entity.Utilisateur;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

@Data
public class Token {
    public static final long DUREE_VALIDITE_JETON = 30 * 60 * 1000; // 30 minutes
    public static final Integer JETON_SIZE = 32;

    private String adresseIp;
    private String userAgent;
    private Utilisateur utilisateur;
    private LocalDateTime dateHeureCreation = LocalDateTime.now();
    private LocalDateTime dateHeureExpiration;
    private String valeur;

    public Token(String adresseIp, String userAgent, Utilisateur utilisateur) {
        setAdresseIp(adresseIp);
        setUserAgent(userAgent);
        setUtilisateur(utilisateur);
        setDateHeureExpiration(getDateHeureCreation().plus(Duration.ofMillis(DUREE_VALIDITE_JETON)));
        genererValeur();
    }

    public void genererValeur() {
        final SecureRandom random = new SecureRandom();
        final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        final byte[] bytes = new byte[JETON_SIZE];
        random.nextBytes(bytes);
        setValeur(base64Encoder.encodeToString(bytes));
    }
}
