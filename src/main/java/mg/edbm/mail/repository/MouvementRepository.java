package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.MailPriority;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MouvementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement, Long>, JpaSpecificationExecutor<Mouvement> {

    @Query("select count(*) from Mouvement m where m.status = ?1 and m.messenger = ?2")
    Long getDeliveringCount(MouvementStatus mouvementStatus, User authenticatedUser);

    @Query("select sum(m.estimatedDelay) from Mouvement m where m.status = ?1 and m.messenger = ?2")
    Double getDeliveringTime(MouvementStatus mailStatus, User authenticatedUser);

    @Query("select sum(m.estimatedDelay) from Mouvement m where m.status = ?1")
    Double getDeliveringTime(MouvementStatus mailStatus);

    @Query("select sum(m.estimatedDistance) from Mouvement m where m.status = ?1 and m.messenger = ?2")
    Double getDeliveringDistance(MouvementStatus MailStatus, User authenticatedUser);

    @Query("select sum(m.estimatedDistance) from Mouvement m where m.status = ?1")
    Double getDeliveringDistance(MouvementStatus mailStatus);

    @Query("select m.mail.createdAt from Mouvement m where m.status = ?1 and m.messenger = ?2 order by m.mail.createdAt desc limit 1")
    LocalDateTime getFirstDeliveringDatetime(MouvementStatus MailStatus, User authenticatedUser);

    @Query("select m.mail.createdAt from Mouvement m where m.status = ?1 order by m.mail.createdAt desc limit 1")
    LocalDateTime getFirstDeliveringDatetime(MouvementStatus MailStatus);

}