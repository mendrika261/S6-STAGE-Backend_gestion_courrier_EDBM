package mg.edbm.gestion_courrier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.gestion_courrier.entity.statut.StatutSession;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "session")
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "token", unique = true)
    private String valeurToken;

    @Column(name = "date_heure_expiration")
    private LocalDateTime dateHeureExpiration;

    @Enumerated
    @Column(name = "statut_session", nullable = false)
    private StatutSession statut = StatutSession.TENTATIVE;

    @Column(name = "date_heure_derniere_activite", nullable = false)
    private LocalDateTime dateHeureDerniereActivite = LocalDateTime.now();

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "adresse_ip", nullable = false)
    private String adresseIp;

    @Column(name = "creation_le", nullable = false)
    private LocalDateTime creationLe = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creation_par_id")
    private Utilisateur creationPar;

    public Session(String valeurToken, StatutSession statut, String userAgent, String adresseIp,
                   Utilisateur creationPar, LocalDateTime dateHeureDerniereActivite, LocalDateTime dateHeureExpiration) {
        setValeurToken(valeurToken);
        setStatut(statut);
        setUserAgent(userAgent);
        setAdresseIp(adresseIp);
        setCreationPar(creationPar);
        setDateHeureDerniereActivite(dateHeureDerniereActivite);
        setDateHeureExpiration(dateHeureExpiration);
    }
}