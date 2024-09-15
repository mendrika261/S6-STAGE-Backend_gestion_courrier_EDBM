package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MouvementRepository extends JpaRepository<Mouvement, Long>, JpaSpecificationExecutor<Mouvement> {
}