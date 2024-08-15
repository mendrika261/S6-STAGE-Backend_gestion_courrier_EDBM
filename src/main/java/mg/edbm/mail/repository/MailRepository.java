package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MailRepository extends JpaRepository<Mail, UUID>, JpaSpecificationExecutor<Mail> {
    @Query("SELECT m.reference FROM Mail m WHERE m.reference LIKE ?1% ORDER BY m.createdAt DESC LIMIT 1")
    String findLastReference(String prefix);

    Optional<Mail> findByIdAndSenderUser(UUID mailId, User senderUser);
}