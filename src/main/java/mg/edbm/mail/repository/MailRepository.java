package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.MailPriority;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MouvementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MailRepository extends JpaRepository<Mail, UUID>, JpaSpecificationExecutor<Mail> {
    @Query("SELECT m.reference FROM Mail m WHERE m.reference LIKE ?1% ORDER BY CAST(SUBSTRING(m.reference, LENGTH(m.reference) - 5, 6) AS integer) DESC LIMIT 1")
    String findLastReference(String prefix);

    Optional<Mail> findByIdAndSenderUser(UUID mailId, User senderUser);

    @Query("SELECT m FROM Mail m WHERE m.id = ?1 AND (m.receiverUser = ?2 OR m.senderUser = ?2)")
    Optional<Mail> findByIdAndReceiverOrSenderUser(UUID mailId, User user);

    Optional<Mail> findByIdAndMouvementsMessengerAndMouvementsStatus(UUID mailId, User messenger, MouvementStatus mouvementStatus);

    Optional<Mail> findByIdAndMouvementsReceiverUser(UUID mailId, User receiverUser);

    Optional<Mail> findByIdAndMouvementsSenderUser(UUID mailId, User receiverUser);


    @Query("select count(*) from Mail m where m.status = ?1 and m.priority = ?2")
    Long getUrgentCount(MailStatus status, MailPriority mailPriority);

    @Query("select count(*) from Mail m where m.status = ?1")
    Long getWaitingCount(MailStatus mailStatus);

    @Query("select count(*) from Mail m where m.status = ?1")
    Long getDeliveringCount(MailStatus MailStatus);
}