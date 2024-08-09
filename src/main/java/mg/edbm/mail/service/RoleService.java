package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.RoleDto;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.entity.Role;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.RoleRepository;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoleService {
    private final RoleRepository roleRepository;

    public Role createOrRestore(RoleDto roleDto, User author) {
        final Role role = new Role(roleDto, author);
        final Optional<Role> removedRole = roleRepository.findIfRemoved(role.getCode());
        if(removedRole.isPresent()) {
            final Role removed = removedRole.get();
            removed.update(roleDto, author);
            removed.restore(author);
            log.info("{} restored {}", author, removed);
            return roleRepository.save(removed);
        }
        log.info("{} created {}", author, role);
        return roleRepository.save(role);
    }

    public Page<Role> list(ListRequest listRequest) {
        listRequest.addCriteria(new SearchCriteria("removedAt", OperationType.EQUAL, null));
        final Pageable pageable = listRequest.toPageable();
        final Specification<Role> specification = new SpecificationImpl<>(listRequest.getCriteria());
        return roleRepository.findAll(specification, pageable);
    }

    public Role get(Long id) throws NotFoundException {
        return roleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Le rôle #" + id + " n'existe pas")
        );
    }

    public Role update(Long id, RoleDto roleDto, User author) throws NotFoundException {
        final Role role = get(id);
        role.update(roleDto, author);
        log.info("{} updated {}", author, role);
        return roleRepository.save(role);
    }

    public Role remove(Long id, User user) throws NotFoundException {
        final Role role = get(id);
        role.remove(user);
        log.info("{} removed {}", user, role);
        return roleRepository.save(role);
    }

    public Set<Role> getRolesFromId(List<Long> roles) {
        return roleRepository.findAllByIdIn(roles);
    }
}
