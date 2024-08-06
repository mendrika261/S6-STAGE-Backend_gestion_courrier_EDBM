package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Session;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
  Session findByTokenValue(String tokenValue);

  @Query("SELECT s FROM Session s WHERE s.user = ?1 AND s.ipAddress = ?2 AND s.userAgent = ?3 AND s.status = ?4")
  Optional<Session> findExistingSession(
          User user, String ipAddress, String userAgent, SessionStatus status
  );
}