package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalisationRepository extends JpaRepository<Location, Long> {
}