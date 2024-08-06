package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.dto.RoleDto;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;


    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private User updatedBy;

    private LocalDateTime updatedAt;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private User removedBy;

    private LocalDateTime removedAt;


    public Role(RoleDto roleDto, User author) {
        setName(roleDto.getName());
        setCode(roleDto.getCode());
        setCreatedBy(author);
    }

    public void update(RoleDto roleDto, User author) {
        setName(roleDto.getName());
        setCode(roleDto.getCode());
        setUpdatedAt(LocalDateTime.now());
        setUpdatedBy(author);
    }

    public void restore(User author) {
        setRemovedAt(null);
        setRemovedBy(null);
        setUpdatedAt(LocalDateTime.now());
        setUpdatedBy(author);
    }

    public void remove(User user) {
        setRemovedAt(LocalDateTime.now());
        setRemovedBy(user);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Role role = (Role) o;
        return getId() != null && Objects.equals(getId(), role.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}