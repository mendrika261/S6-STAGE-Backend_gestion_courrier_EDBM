package mg.edbm.mail.repository;

import mg.edbm.mail.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AppRepository extends JpaRepository<App, UUID>, JpaSpecificationExecutor<App> {
    @Query("SELECT a FROM App a WHERE a.removedAt IS NULL")
    List<App> findAllActive();
}
