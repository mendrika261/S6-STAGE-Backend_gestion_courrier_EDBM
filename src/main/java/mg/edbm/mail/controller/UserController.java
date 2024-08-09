package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.DatabaseConfig;
import mg.edbm.mail.dto.request.PasswordDtoRequest;
import mg.edbm.mail.dto.response.UserDtoResponse;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.UserDtoRequest;
import mg.edbm.mail.dto.response.FormResponse;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.UserStatus;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.exception.ValidationException;
import mg.edbm.mail.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Transactional
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDtoResponse> create(@Valid UserDtoRequest userDtoRequest) throws AuthenticationException, ValidationException {
        final User user = userService.create(userDtoRequest, userService.getAuthenticatedUser());
        final UserDtoResponse mappedUserDtoResponse = new UserDtoResponse(user);
        return ResponseEntity.ok(mappedUserDtoResponse);
    }

    @GetMapping
    public ResponseEntity<Page<UserDtoResponse>> list(@Valid ListRequest listRequest) {
        final Page<User> users = userService.list(listRequest);
        final Page<UserDtoResponse> mappedUserDtoList = users.map(UserDtoResponse::new);
        return ResponseEntity.ok(mappedUserDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> get(@PathVariable UUID id) {
        final User user = userService.getUser(id);
        final UserDtoResponse mappedUserDtoResponse = new UserDtoResponse(user);
        return ResponseEntity.ok(mappedUserDtoResponse);
    }

    @GetMapping("/{id}/password")
    public ResponseEntity<UserDtoResponse> resetPassword(@PathVariable UUID id) throws NotFoundException, ValidationException {
        final User user = userService.resetPassword(id);
        final UserDtoResponse mappedUserDtoResponse = new UserDtoResponse(user);
        return ResponseEntity.ok(mappedUserDtoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDtoResponse> update(@PathVariable UUID id, @Valid UserDtoRequest userDtoRequest)
            throws NotFoundException, AuthenticationException {
        final User user = userService.updateWithoutPassword(id, userDtoRequest, userService.getAuthenticatedUser());
        final UserDtoResponse mappedUserDtoResponse = new UserDtoResponse(user);
        return ResponseEntity.ok(mappedUserDtoResponse);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserDtoResponse> updatePassword(@PathVariable UUID id, @Valid PasswordDtoRequest passwordDtoRequest)
            throws NotFoundException, AuthenticationException {
        final User user = userService.updatePassword(id, passwordDtoRequest, userService.getAuthenticatedUser());
        final UserDtoResponse mappedUserDtoResponse = new UserDtoResponse(user);
        return ResponseEntity.ok(mappedUserDtoResponse);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserDtoResponse> updateStatus(@PathVariable UUID id,
                                                        UserStatus status)
            throws NotFoundException, AuthenticationException, ValidationException {
        final User user = userService.updateStatus(id, status, userService.getAuthenticatedUser());
        final UserDtoResponse mappedUserDtoResponse = new UserDtoResponse(user);
        return ResponseEntity.ok(mappedUserDtoResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FormResponse> handleDataIntegrityException(DataIntegrityViolationException ex) {
        final FormResponse formResponse = new FormResponse();
        if(ex.getMessage().contains(DatabaseConfig.UNIQUE_ERROR_CONSTRAINT))
            formResponse.addFieldErrors("email", "Cette adresse email est déjà utilisée");
        return ResponseEntity.badRequest().body(formResponse);
    }
}
