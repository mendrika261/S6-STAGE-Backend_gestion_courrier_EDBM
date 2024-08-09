package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Location;
import mg.edbm.mail.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
  @Query("SELECT l FROM Location l WHERE l.latitude = :latitude AND l.longitude = :longitude AND l.removedAt IS NOT NULL ORDER BY l.removedAt DESC LIMIT 1")
  Optional<Location> findIfRemoved(Double latitude, Double longitude);

  @Override
  @NonNull
  @Query("SELECT l FROM Location l WHERE l.id = :id AND l.removedAt IS NULL ORDER BY l.createdAt DESC LIMIT 1")
  Optional<Location> findById(@NonNull Long id);
}