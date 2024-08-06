package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.edbm.mail.entity.type.UserStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(nullable = false)
    private User user;


    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    private String email;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.WORKING;


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