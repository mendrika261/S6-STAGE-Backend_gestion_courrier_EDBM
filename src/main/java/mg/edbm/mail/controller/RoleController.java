package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.DatabaseConfig;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.RoleDto;
import mg.edbm.mail.dto.response.FormResponse;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.entity.Role;
import mg.edbm.mail.service.RoleService;
import mg.edbm.mail.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Transactional
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;

    @PostMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<RoleDto> create(@Valid RoleDto roleDto) throws AuthenticationException {
        final Role role = roleService.createOrRestore(roleDto, userService.getAuthenticatedUser());
        final RoleDto mappedRoleDto = new RoleDto(role);
        return ResponseEntity.ok(mappedRoleDto);
    }

    @GetMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<Page<RoleDto>> list(@Valid ListRequest listRequest) {
        final Page<RoleDto> roles = roleService.list(listRequest).map(RoleDto::new);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDto> get(@PathVariable Long roleId) throws NotFoundException {
        final Role role = roleService.get(roleId);
        final RoleDto roleDto = new RoleDto(role);
        return ResponseEntity.ok(roleDto);
    }

    @PutMapping("/{roleId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<RoleDto> update(@PathVariable Long roleId, @Valid RoleDto roleDto) throws NotFoundException,
            AuthenticationException {
        final Role role = roleService.update(roleId, roleDto, userService.getAuthenticatedUser());
        final RoleDto mappedRoleDto = new RoleDto(role);
        return ResponseEntity.ok(mappedRoleDto);
    }

    @DeleteMapping("/{roleId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<RoleDto> remove(@PathVariable Long roleId) throws NotFoundException, AuthenticationException {
        final Role role = roleService.remove(roleId, userService.getAuthenticatedUser());
        final RoleDto mappedRoleDto = new RoleDto(role);
        return ResponseEntity.ok(mappedRoleDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FormResponse> handleDataIntegrityException(DataIntegrityViolationException ex) {
        final FormResponse formResponse = new FormResponse();
        if(ex.getMessage().contains(DatabaseConfig.UNIQUE_ERROR_CONSTRAINT))
            formResponse.addFieldErrors("code", "Le rôle avec ce code existe déjà");
        return ResponseEntity.badRequest().body(formResponse);
    }
}
