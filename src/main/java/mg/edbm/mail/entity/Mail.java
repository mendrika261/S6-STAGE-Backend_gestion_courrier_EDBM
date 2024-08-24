package mg.edbm.mail.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.dto.request.MailOutgoingRequest;
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
@NoArgsConstructor
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
    private MailStatus status = MailStatus.DRAFT;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Location senderLocation;

    @Column
    private String sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User senderUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Location receiverLocation;

    @Column
    private String receiver;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User receiverUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<File> files = new LinkedHashSet<>();

    @Lob
    private String noteForMessenger;

    @Lob
    private String description;


    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(nullable = false)
    private User createdBy;

    public Mail(MailOutgoingRequest mailOutgoingRequest, User senderUser, User author) {
        setObject(mailOutgoingRequest.getObject());
        setConfidentiality(mailOutgoingRequest.getConfidentiality());
        setPriority(mailOutgoingRequest.getMailPriority());
        setNoteForMessenger(mailOutgoingRequest.getNoteForMessenger());
        setDescription(mailOutgoingRequest.getDescription());
        setSenderUser(senderUser);
        setCreatedBy(author);
    }

    public void update(@Valid MailOutgoingRequest mailOutgoingRequest, User authenticatedUser) {
        setObject(mailOutgoingRequest.getObject());
        setConfidentiality(mailOutgoingRequest.getConfidentiality());
        setPriority(mailOutgoingRequest.getMailPriority());
        setNoteForMessenger(mailOutgoingRequest.getNoteForMessenger());
        setDescription(mailOutgoingRequest.getDescription());
        setCreatedBy(authenticatedUser);
    }

    public void addFile(File file) {
        files.add(file);
    }
}