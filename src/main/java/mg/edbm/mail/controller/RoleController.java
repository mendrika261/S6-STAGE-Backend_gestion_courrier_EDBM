package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.DatabaseConfig;
import mg.edbm.mail.dto.request.PaginationRequest;
import mg.edbm.mail.dto.RoleDto;
import mg.edbm.mail.dto.response.FormResponse;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.entity.Role;
import mg.edbm.mail.service.RoleService;
import mg.edbm.mail.service.UserService;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;

    @Transactional
    @PostMapping
    public ResponseEntity<RoleDto> create(@Valid RoleDto roleDto) throws AuthenticationException {
        final Role role = roleService.create(roleDto, userService.getAuthenticatedUser());
        final RoleDto mappedRoleDto = new RoleDto(role);
        return ResponseEntity.ok(mappedRoleDto);
    }

    @GetMapping
    public ResponseEntity<Page<RoleDto>> list(@Valid PaginationRequest paginationRequest) {
        List<SearchCriteria> params = new ArrayList<>();
        final Page<Role> roles = roleService.list(params, paginationRequest);
        final Page<RoleDto> mappedRoleDtoList = roles.map(RoleDto::new);
        return ResponseEntity.ok(mappedRoleDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> get(@PathVariable Long id) throws NotFoundException {
        final Role role = roleService.getRole(id);
        final RoleDto roleDto = new RoleDto(role);
        return ResponseEntity.ok(roleDto);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> update(@PathVariable Long id, @Valid RoleDto roleDto) throws NotFoundException,
            AuthenticationException {
        final Role role = roleService.update(id, roleDto, userService.getAuthenticatedUser());
        final RoleDto mappedRoleDto = new RoleDto(role);
        return ResponseEntity.ok(mappedRoleDto);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<RoleDto> remove(@PathVariable Long id) throws NotFoundException, AuthenticationException {
        final Role role = roleService.remove(id, userService.getAuthenticatedUser());
        final RoleDto mappedRoleDto = new RoleDto(role);
        return ResponseEntity.ok(mappedRoleDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FormResponse> handleDataIntegrityException(DataIntegrityViolationException ex) {
        final FormResponse formResponse = new FormResponse("Veuillez bien vérifier votre saisie");
        if(ex.getMessage().contains(DatabaseConfig.UNIQUE_ERROR_CONSTRAINT))
            formResponse.setGlobalMessages("Le rôle avec ce code existe déjà");
        return ResponseEntity.badRequest().body(formResponse);
    }
}
