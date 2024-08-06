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

@Getter
@Setter
@Entity
@Table
public class MailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(nullable = false)
    private Mail mail;


    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private String object;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailConfidentiality mailConfidentiality = MailConfidentiality.PRIVATE;

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
    @JoinColumn(nullable = false)
    private Location receiverLocation;

    @Column(nullable = false)
    private String receiver;

    @JoinColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<File> files = new LinkedHashSet<>();

    private String noteForMessenger;

    private String description;


    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private User removedBy;

    private LocalDateTime removedAt;
}