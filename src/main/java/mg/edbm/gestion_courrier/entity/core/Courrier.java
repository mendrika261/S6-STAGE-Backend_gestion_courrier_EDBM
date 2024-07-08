package mg.edbm.gestion_courrier.entity.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courrier")
public class Courrier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @Column(name = "objet", nullable = false)
    private String objet;

    @Column(name = "confidentialite", nullable = false)
    private Integer confidentialite = 0;

    @Column(name = "urgence", nullable = false)
    private Integer urgence = 0;

    @Column(name = "statut", nullable = false)
    private Integer statut = 0;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "depart_id", nullable = false)
    private Localisation depart;

    @Column(name = "expediteur", nullable = false)
    private String expediteur;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private Localisation destination;

    @Column(name = "destinataire", nullable = false)
    private String destinataire;

    @OneToMany(mappedBy = "courrier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Fichier> fichiers = new LinkedHashSet<>();


    @Column(name = "creation_le", nullable = false)
    private LocalDateTime creationLe = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "creation_par_id", nullable = false)
    private Utilisateur creationPar;
}