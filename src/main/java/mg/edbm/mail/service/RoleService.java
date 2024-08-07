package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.request.PaginationRequest;
import mg.edbm.mail.dto.RoleDto;
import mg.edbm.mail.entity.Role;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.RoleRepository;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoleService {
    private final RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role create(RoleDto roleDto, User author) {
        final Role role = new Role(roleDto, author);
        final Optional<Role> removedRole = roleRepository.findIfRemoved(role.getCode());
        if(removedRole.isPresent()) {
            final Role removed = removedRole.get();
            removed.update(roleDto, author);
            removed.restore(author);
            log.info("{} restored {}", author, removed);
            return save(removed);
        }
        log.info("{} created {}", author, role);
        return save(role);
    }

    public Page<Role> list(List<SearchCriteria> params, PaginationRequest paginationRequest) {
        final Pageable pageable = paginationRequest.toPageable();
        final Specification<Role> specification = new SpecificationImpl<>(params);
        return roleRepository.findAll(specification, pageable);
    }

    public Role getRole(Long id) throws NotFoundException {
        return roleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Le rôle #" + id + " n'existe pas")
        );
    }

    public Role update(Long id, RoleDto roleDto, User author) throws NotFoundException {
        final Role role = getRole(id);
        role.update(roleDto, author);
        log.info("{} updated {}", author, role);
        return save(role);
    }

    public Role remove(Long id, User user) throws NotFoundException {
        final Role role = getRole(id);
        role.remove(user);
        log.info("{} removed {}", user, role);
        return save(role);
    }
}
