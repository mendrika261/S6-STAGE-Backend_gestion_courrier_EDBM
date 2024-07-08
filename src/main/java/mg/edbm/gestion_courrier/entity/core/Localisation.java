package mg.edbm.gestion_courrier.entity.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "localisation", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"latitude", "longitude"})
})
public class Localisation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;


    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "creation_par_id", nullable = false)
    private Utilisateur creationPar;

    @Column(name = "creation_le", nullable = false)
    private LocalDateTime creationLe = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "modification_par_id")
    private Utilisateur modificationPar;

    @Column(name = "modification_le")
    private LocalDateTime modificationLe;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "suppression_par_id")
    private Utilisateur suppressionPar;

    @Column(name = "suppression_le")
    private LocalDateTime suppressionLe;
}