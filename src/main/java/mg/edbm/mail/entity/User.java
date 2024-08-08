package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.dto.request.UserDtoRequest;
import mg.edbm.mail.entity.type.UserStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "system_user")
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.CREATED;

    @JoinTable
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Role> roles = new LinkedHashSet<>();

    @Column( nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private User createdBy;

    public User(UserDtoRequest userDtoRequest, User authenticatedUser) {
        setEmail(userDtoRequest.getEmail());
        setLastName(userDtoRequest.getLastName());
        setFirstName(userDtoRequest.getFirstName());
        setPhoneNumber(userDtoRequest.getPhoneNumber());
        setPassword(userDtoRequest.getPassword());
        setCreatedBy(authenticatedUser);
    }

    public User(User user) {
        setId(user.getId());
        setEmail(user.getEmail());
        setLastName(user.getLastName());
        setFirstName(user.getFirstName());
        setPhoneNumber(user.getPhoneNumber());
        setPassword(user.getPassword());
        setStatus(user.getStatus());
        setRoles(user.getRoles());
        setCreatedAt(user.getCreatedAt());
        setCreatedBy(user.getCreatedBy());
    }

    public boolean isActive() {
        return getStatus().equals(UserStatus.ACTIVE);
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

    @Override
    public String toString() {
        return getEmail();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}