package mg.edbm.mail.repository;

import mg.edbm.mail.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppRepository extends JpaRepository<App, UUID>, JpaSpecificationExecutor<App> {
    @Query("SELECT a FROM App a WHERE a.removedAt IS NULL")
    List<App> findAllActive();

    @Query("SELECT a FROM App a WHERE a.name = :name AND a.url = :url AND a.removedAt IS NOT NULL ORDER BY a.removedAt DESC LIMIT 1")
    Optional<App> findIfRemoved(String name, String url);
}
