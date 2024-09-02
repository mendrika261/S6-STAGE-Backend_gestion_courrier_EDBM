package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.entity.type.MouvementStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Mouvement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User messenger;

    @Column(nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column
    private LocalDateTime endDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Location senderLocation;

    @Column(nullable = false)
    private String sender;

    @ManyToOne
    @JoinColumn
    private User senderUser;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Location receiverLocation;

    @Column(nullable = false)
    private String receiver;

    @ManyToOne
    @JoinColumn
    private User receiverUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MouvementStatus status = MouvementStatus.WAITING;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn
    private Mail mail;

    public Mouvement(Mail mail, User messenger) {
        setMessenger(messenger);
        setSenderLocation(mail.getSenderLocation());
        setSender(mail.getSender());
        setSenderUser(mail.getSenderUser());
        setReceiverLocation(mail.getReceiverLocation());
        setReceiver(mail.getReceiver());
        setReceiverUser(mail.getReceiverUser());
        setMail(mail);
    }
}