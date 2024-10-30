package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Session;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID>, JpaSpecificationExecutor<Session> {
    @Query("SELECT s FROM Session s WHERE s.tokenValue = ?1 ORDER BY s.createdAt DESC LIMIT 1")
    Session findByTokenValue(String tokenValue);

    @Query("""
      SELECT s FROM Session s
      WHERE s.user = ?1 AND s.ipAddress = ?2 AND s.userAgent = ?3 AND s.status = ?4
      ORDER BY s.createdAt DESC LIMIT 1
    """)
      Optional<Session> findExistingSession(
              User user, String ipAddress, String userAgent, SessionStatus status
      );

    @Query("""
        SELECT s FROM Session s
        WHERE s.user = ?1 AND s.ipAddress = ?2 AND s.userAgent = ?3
        ORDER BY s.createdAt DESC LIMIT 1
    """)
    Optional<Session> findLastSession(User user, String ipAddress, String userAgent);

    @Modifying
    @Query("""
        UPDATE Session s
        SET s.status = ?3
        WHERE s.user = ?1 AND s.status = ?2
    """)
    void updateAllActiveSessionOf(User user, SessionStatus sessionStatus, SessionStatus newSessionStatus);

    Long countSessionByStatus(SessionStatus sessionStatus);

    @Query("""
        SELECT AVG (s.queryCount)
        FROM Session s
    """)
    Long averageQueryCountPerSession();
}