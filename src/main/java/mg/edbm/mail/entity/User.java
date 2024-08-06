package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.edbm.mail.entity.type.UserStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "system_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @Enumerated
    @Column(nullable = false)
    private UserStatus status = UserStatus.WORKING;

    @JoinTable
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Role> roles = new LinkedHashSet<>();


    @Column( nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private User createdBy;

    public boolean isActive() {
        return getStatus().equals(UserStatus.WORKING);
    }

    public String[] getRolesCode() {
        return getRoles().stream().map(Role::getCode).toArray(String[]::new);
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return Arrays.stream(getRolesCode()).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}