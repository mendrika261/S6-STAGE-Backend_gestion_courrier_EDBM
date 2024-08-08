package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.DatabaseConfig;
import mg.edbm.mail.dto.response.UserDtoResponse;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.UserDtoRequest;
import mg.edbm.mail.dto.response.FormResponse;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity<UserDtoResponse> create(@Valid UserDtoRequest userDtoRequest) throws AuthenticationException {
        final User user = userService.create(userDtoRequest, userService.getAuthenticatedUser());
        final UserDtoResponse mappedUserDtoResponse = new UserDtoResponse(user, true);
        return ResponseEntity.ok(mappedUserDtoResponse);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<Page<UserDtoResponse>> list(@Valid ListRequest listRequest) {
        final Page<User> users = userService.list(listRequest);
        final Page<UserDtoResponse> mappedUserDtoList = users.map(UserDtoResponse::new);
        return ResponseEntity.ok(mappedUserDtoList);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FormResponse> handleDataIntegrityException(DataIntegrityViolationException ex) {
        final FormResponse formResponse = new FormResponse();
        if(ex.getMessage().contains(DatabaseConfig.UNIQUE_ERROR_CONSTRAINT))
            formResponse.addFieldErrors("email", "Cette adresse email est déjà utilisée");
        return ResponseEntity.badRequest().body(formResponse);
    }
}
