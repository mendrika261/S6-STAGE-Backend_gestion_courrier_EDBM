package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "app")
public class App {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logoUrl", nullable = true)
    private String logoUrl;

    @Column(name = "url", nullable = false, unique = true)
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
}