package mg.edbm.mail.repository;

import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.MouvementStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findById(@NonNull UUID uuid);

    @Query(value = "SELECT u.* " +
            "FROM system_user u " +
            "LEFT JOIN mouvement m ON u.id = m.messenger_id AND m.status = 'DELIVERING' " +
            "JOIN system_user_roles ur ON u.id = ur.user_id " +
            "JOIN role r ON ur.roles_id = r.id AND r.code = 'MAIL_MESSENGER' " +
            "WHERE u.status = 'ACTIVE'  " +
            "  AND m.messenger_id IS NULL;",
            nativeQuery = true)
    List<User> findFreeMessengers();
}