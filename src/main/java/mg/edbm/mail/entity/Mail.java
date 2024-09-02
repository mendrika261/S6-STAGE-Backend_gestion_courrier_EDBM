package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.request.MailOutgoingRequest;
import mg.edbm.mail.entity.type.MailConfidentiality;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MailPriority;
import mg.edbm.mail.entity.type.MouvementStatus;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.*;

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

    @ManyToOne
    @JoinColumn
    private Location senderLocation;

    @Column
    private String sender;

    @ManyToOne
    @JoinColumn
    private User senderUser;

    @ManyToOne
    @JoinColumn
    private Location receiverLocation;

    @Column
    private String receiver;

    @ManyToOne
    @JoinColumn
    private User receiverUser;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<File> files = new ArrayList<>();

    @Lob
    private String noteForMessenger;

    @Lob
    private String description;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true, mappedBy = "mail")
    @OrderBy("startDate DESC")
    private List<Mouvement> mouvements = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User createdBy;

    public Mail(MailOutgoingRequest mailOutgoingRequest, User senderUser, User author) {
        setObject(mailOutgoingRequest.getObject());
        setConfidentiality(mailOutgoingRequest.getConfidentiality());
        setPriority(mailOutgoingRequest.getMailPriority());
        setNoteForMessenger(mailOutgoingRequest.getNoteForMessenger());
        setDescription(mailOutgoingRequest.getDescription());
        setSenderUser(senderUser);
        setSenderLocation(senderUser.getLocation());
        setSender(senderUser.getFullName());
        setCreatedBy(author);
    }

    public void updateIfAuthorized(MailOutgoingRequest mailOutgoingRequest, User author) {
        verifyIfEditableBy(author);
        setObject(mailOutgoingRequest.getObject());
        setConfidentiality(mailOutgoingRequest.getConfidentiality());
        setPriority(mailOutgoingRequest.getMailPriority());
        setNoteForMessenger(mailOutgoingRequest.getNoteForMessenger());
        setDescription(mailOutgoingRequest.getDescription());
        setCreatedBy(author);
        setCreatedAt(LocalDateTime.now());
    }

    public void updateStatusIfAuthorized(MailStatus mailStatus, User author) {
        verifyIfEditableBy(author);
        setStatus(mailStatus);
        setCreatedBy(author);
        setCreatedAt(LocalDateTime.now());
    }

    public void verifyIfEditableBy(User user) {
        if(user.getRoles().contains(SecurityConfig.ROLE_ADMIN))
            return;
        if(user.getRoles().contains(SecurityConfig.ROLE_MESSENGER) && getStatus().getCode() < MailStatus.WAITING.getCode())
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier ce courrier " +
                    "tant qu'il n'est pas encore en cours de traitement");
        if(getStatus().getCode() > MailStatus.WAITING.getCode())
            throw new AccessDeniedException("Vous ne pouvez plus modifier ce courrier car " +
                    "il est déjà en cours de traitement");
    }


    public void addFile(File file) {
        getFiles().add(file);
    }

    public void removeFile(File file) {
        getFiles().remove(file);
    }

    public void addMouvement(Mouvement mouvement) {
        getMouvements().add(mouvement);
    }

    public Mouvement removeLastMouvement() {
        return getMouvements().remove(0);
    }

    public void signMouvementStart(LocalDateTime startDate, User author) {
        getMouvements().get(0).setStartDate(startDate);
        getMouvements().get(0).setStatus(MouvementStatus.DELIVERING);
        setStatus(MailStatus.DELIVERING);
        setCreatedBy(author);
        setCreatedAt(startDate);
    }
}