package mg.edbm.mail.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.TokenDto;
import mg.edbm.mail.dto.request.AuthenticationRequest;
import mg.edbm.mail.dto.response.UserResponse;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.Token;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.exception.ValidationException;
import mg.edbm.mail.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final UserService userService;

    @PostMapping("/auth")
    @Transactional(noRollbackFor = AuthenticationException.class)
    public ResponseEntity<TokenDto> authenticateWithPassword(
            @Valid AuthenticationRequest authenticationRequest, HttpServletRequest request
    ) throws AuthenticationException {
        final Token token = userService.authenticateWithPassword(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword(),
                request
        );
        final TokenDto tokenDto = new TokenDto(token);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/auth/password-recovery")
    public ResponseEntity<UserResponse> recoverPassword(String email) throws ValidationException, NotFoundException {
        final User user = userService.resetPasswordByEmail(email);
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }
}
