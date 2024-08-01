package mg.edbm.gestion_courrier.repository;

import mg.edbm.gestion_courrier.entity.Localisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalisationRepository extends JpaRepository<Localisation, Long> {
}