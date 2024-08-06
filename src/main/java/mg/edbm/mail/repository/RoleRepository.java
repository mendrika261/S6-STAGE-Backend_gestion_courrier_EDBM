package mg.edbm.mail.repository;

import mg.edbm.mail.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    @Override
    @NonNull
    @Query("SELECT r FROM Role r WHERE r.removedAt IS NULL")
    Page<Role> findAll(@NonNull Specification<Role> spec, @NonNull Pageable pageable);

    @Override
    @NonNull
    @Query("SELECT r FROM Role r WHERE r.id = :id AND r.removedAt IS NULL")
    Optional<Role> findById(@NonNull Long id);

    @Query("SELECT r FROM Role r WHERE r.code = :code AND r.removedAt IS NOT NULL")
    Optional<Role> findIfRemoved(String code);
}
