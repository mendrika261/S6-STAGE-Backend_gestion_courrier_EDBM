package mg.edbm.gestion_courrier.entity.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "historique_fichier")
public class HistoriqueFichier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "fichier_id", nullable = false)
    private Fichier fichier;


    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "chemin", nullable = false)
    private String chemin;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "courrier_id", nullable = false)
    private Courrier courrier;


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