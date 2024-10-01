package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.dto.AppDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "app", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "url")
})
@NoArgsConstructor
public class App {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logoUrl")
    private String logoUrl;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "authorized_role_prefix", nullable = false)
    private String authorizedRolePrefix;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User updatedBy;

    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User removedBy;

    private LocalDateTime removedAt;

    public App(AppDto appDto, User author) {
        setName(appDto.getName());
        setDescription(appDto.getDescription());
        setLogoUrl(appDto.getLogoUrl());
        setUrl(appDto.getUrl());
        setAuthorizedRolePrefix(appDto.getAuthorizedRolePrefix());
        setCreatedBy(author);
    }

    public void update(AppDto appDto, User author) {
        setName(appDto.getName());
        setDescription(appDto.getDescription());
        setLogoUrl(appDto.getLogoUrl());
        setUrl(appDto.getUrl());
        setAuthorizedRolePrefix(appDto.getAuthorizedRolePrefix());
        setUpdatedBy(author);
        setUpdatedAt(LocalDateTime.now());
    }

    public void delete(User author) {
        setRemovedBy(author);
        setRemovedAt(LocalDateTime.now());
    }

    public void restore(User author) {
        setRemovedBy(null);
        setRemovedAt(null);
        setUpdatedBy(author);
        setUpdatedAt(LocalDateTime.now());
    }
}