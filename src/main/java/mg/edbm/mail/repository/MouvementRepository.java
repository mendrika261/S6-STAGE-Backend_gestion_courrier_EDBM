package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.MailPriority;
import mg.edbm.mail.entity.type.MouvementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MouvementRepository extends JpaRepository<Mouvement, Long>, JpaSpecificationExecutor<Mouvement> {
    @Query("select count(*) from Mouvement m where m.status = ?1 and m.mail.priority = ?2")
    Long getUrgentCount(MouvementStatus status, MailPriority mailPriority);

    @Query("select count(*) from Mouvement m where m.status = ?1")
    Long getWaitingCount(MouvementStatus mouvementStatus);

    @Query("select count(*) from Mouvement m where m.status = ?1 and m.messenger = ?2")
    Long getDeliveringCount(MouvementStatus mouvementStatus, User authenticatedUser);

    @Query("select sum(m.estimatedDelay) from Mouvement m where m.status = ?1 and m.messenger = ?2")
    Double getDeliveringTime(MouvementStatus mouvementStatus, User authenticatedUser);

    @Query("select sum(m.estimatedDistance) from Mouvement m where m.status = ?1 and m.messenger = ?2")
    Double getDeliveringDistance(MouvementStatus mouvementStatus, User authenticatedUser);

    @Query("select m.mail.createdAt from Mouvement m where m.status = ?1 and m.messenger = ?2 order by m.mail.createdAt desc limit 1")
    LocalDateTime getFirstDeliveringDatetime(MouvementStatus mouvementStatus, User authenticatedUser);
}