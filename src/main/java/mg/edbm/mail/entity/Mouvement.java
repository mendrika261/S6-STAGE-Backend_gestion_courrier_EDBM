package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.entity.type.MouvementStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Double estimatedDelay;
    private Double estimatedDistance;
    @Lob
    private String coordinates;

    public Mouvement(Mail mail, User messenger) {
        setMessenger(messenger);
        if(mail.getMouvements().isEmpty()) {
            setSenderLocation(mail.getSenderLocation());
            setSender(mail.getSender());
            setSenderUser(mail.getSenderUser());
        } else {
            final Mouvement lastMouvement = mail.getLastMouvement();
            setSenderLocation(lastMouvement.getReceiverLocation());
            setSender(lastMouvement.getReceiver());
            setSenderUser(lastMouvement.getReceiverUser());
        }
        setReceiverLocation(mail.getReceiverLocation());
        setReceiver(mail.getReceiver());
        setReceiverUser(mail.getReceiverUser());
        if(getSenderUser() == null)
            setStatus(MouvementStatus.DELIVERING);
        setMail(mail);
    }

    public void setCoordinates(List<Double[]> coordinates) {
        StringBuilder builder = new StringBuilder();
        for (Double[] coordinate : coordinates) {
            builder.append(coordinate[0]).append(",").append(coordinate[1]).append(";");
        }
        this.coordinates = builder.toString();
    }

    public List<Double[]> getCoordinatesAsList() {
        final List<Double[]> coordinates = new ArrayList<>();
        if (this.getCoordinates() == null || getCoordinates().isEmpty()) {
            return coordinates;
        }
        final String[] split = getCoordinates().split(";");
        for (String s : split) {
            final String[] split1 = s.split(",");
            final Double[] coord = new Double[2];
            coord[0] = Double.parseDouble(split1[0]);
            coord[1] = Double.parseDouble(split1[1]);
            coordinates.add(coord);
        }
        return coordinates;
    }
}