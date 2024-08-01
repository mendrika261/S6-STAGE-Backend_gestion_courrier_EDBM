package mg.edbm.gestion_courrier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.edbm.gestion_courrier.entity.statut.StatutUtilisateur;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "contact")
    private String contact;

    @Enumerated
    @Column(name = "statut", nullable = false)
    private StatutUtilisateur statut = StatutUtilisateur.ACTIF;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "utilisateur_role",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();


    @Column(name = "creation_le", nullable = false)
    private LocalDateTime creationLe = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creation_par_id")
    private Utilisateur creationPar;

    public boolean estValide() {
        return statut.equals(StatutUtilisateur.ACTIF);
    }

    public String[] getRolesCode() {
        return roles.stream().map(Role::getCode).toArray(String[]::new);
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return Arrays.stream(getRolesCode()).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return getMotDePasse();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}