package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.RoleDto;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.type.LogicOperationType;
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
        listRequest.addBaseCriteria("removedAt", OperationType.EQUAL, null);
        final Pageable pageable = listRequest.toPageable();
        final Specification<Role> specification = new SpecificationImpl<>(listRequest);
        return roleRepository.findAll(specification, pageable);
    }

    public Role get(Long roleId) throws NotFoundException {
        return roleRepository.findById(roleId).orElseThrow(
                () -> new NotFoundException("Le rôle #" + roleId + " n'existe pas")
        );
    }

    public Role update(Long roleId, RoleDto roleDto, User author) throws NotFoundException {
        final Role role = get(roleId);
        role.update(roleDto, author);
        log.info("{} updated {}", author, role);
        return roleRepository.save(role);
    }

    public Role remove(Long roleId, User user) throws NotFoundException {
        final Role role = get(roleId);
        role.remove(user);
        log.info("{} removed {}", user, role);
        return roleRepository.save(role);
    }

    public Set<Role> getRolesFromCodes(List<String> rolesCode) {
        return roleRepository.findAllByCodeIn(rolesCode);
    }
}
