package mg.edbm.gestion_courrier.entity.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "historique_utilisateur")
public class HistoriqueUtilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;


    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "email")
    private String email;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "contact")
    private String contact;

    @Column(name = "statut", nullable = false)
    private Integer statut = 0;


    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "creation_par_id", nullable = false)
    private Utilisateur creationPar;

    @Column(name = "creation_le", nullable = false)
    private LocalDateTime creationLe = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "suppression_par_id")
    private Utilisateur suppressionPar;

    @Column(name = "suppression_le")
    private LocalDateTime suppressionLe;
}