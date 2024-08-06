package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.edbm.mail.entity.type.MailConfidentiality;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MailPriority;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private String object;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailConfidentiality confidentiality = MailConfidentiality.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailPriority priority = MailPriority.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailStatus status = MailStatus.WAITING;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(nullable = false)
    private Location senderLocation;

    @Column(nullable = false)
    private String sender;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn( nullable = false)
    private Location receiverLocation;

    @Column(nullable = false)
    private String receiver;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<File> files = new LinkedHashSet<>();

    private String noteForMessenger;

    private String description;


    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn( nullable = false)
    private User createdBy;
}