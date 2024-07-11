package mg.edbm.gestion_courrier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "fichier", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"chemin", "nom", "type"})
})
public class Fichier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "chemin", nullable = false)
    private String chemin;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "courrier_id", nullable = false)
    private Courrier courrier;


    @Column(name = "creation_le", nullable = false)
    private LocalDateTime creationLe = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "creation_par_id", nullable = false)
    private Utilisateur creationPar;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Fichier fichier = (Fichier) o;
        return getId() != null && Objects.equals(getId(), fichier.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}