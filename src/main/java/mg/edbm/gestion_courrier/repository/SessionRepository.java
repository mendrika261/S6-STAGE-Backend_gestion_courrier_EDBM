package mg.edbm.gestion_courrier.repository;

import mg.edbm.gestion_courrier.entity.Session;
import mg.edbm.gestion_courrier.entity.Utilisateur;
import mg.edbm.gestion_courrier.entity.statut.StatutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
  Session findByValeurToken(String valeurToken);

  Session findByCreationParAndAdresseIpAndUserAgentAndStatut(
          Utilisateur utilisateur,
          String adresseIp,
          String userAgent,
          StatutSession statut
  );
}