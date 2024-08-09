package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MailRepository extends JpaRepository<Mail, UUID>, JpaSpecificationExecutor<Mail> {
}