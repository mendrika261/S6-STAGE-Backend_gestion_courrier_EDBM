package mg.edbm.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.dto.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"latitude", "longitude"})
})
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;


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

    public Location(LocationDto locationDto, User author) {
        setName(locationDto.getName());
        setLatitude(locationDto.getLatitude());
        setLongitude(locationDto.getLongitude());
        setCreatedBy(author);
    }

    public void update(LocationDto locationDto, User author) {
        setName(locationDto.getName());
        setLatitude(locationDto.getLatitude());
        setLongitude(locationDto.getLongitude());
        setUpdatedBy(author);
        setUpdatedAt(LocalDateTime.now());
    }

    public void restore(User author) {
        setRemovedBy(null);
        setRemovedAt(null);
        setUpdatedBy(author);
        setUpdatedAt(LocalDateTime.now());
    }

    public void remove(User authenticatedUser) {
        setRemovedBy(authenticatedUser);
        setRemovedAt(LocalDateTime.now());
    }
}