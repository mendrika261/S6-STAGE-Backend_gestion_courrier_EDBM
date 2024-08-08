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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    @Override
    @NonNull
    Page<Role> findAll(@NonNull Specification<Role> spec, @NonNull Pageable pageable);

    @Override
    @NonNull
    @Query("SELECT r FROM Role r WHERE r.id = :id AND r.removedAt IS NULL ORDER BY r.createdAt DESC LIMIT 1")
    Optional<Role> findById(@NonNull Long id);

    @Query("SELECT r FROM Role r WHERE r.code = :code AND r.removedAt IS NOT NULL ORDER BY r.removedAt DESC LIMIT 1")
    Optional<Role> findIfRemoved(String code);

    @Query("SELECT r FROM Role r WHERE r.id IN :ids AND r.removedAt IS NULL")
    Set<Role> findAllByIdIn(List<Long> ids);
}
