package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserService userService;

    public boolean hasAdminRoleOrIsSelf(UUID id) throws AuthenticationException {
        User authenticatedUser = userService.getAuthenticatedUser();
        return Arrays.stream(authenticatedUser.getRolesCode()).toList().contains(SecurityConfig.ROLE_ADMIN) ||
                authenticatedUser.getId().equals(id);
    }
}
