package mg.edbm.mail.utils;

import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class UserUtils {
    public static UUID getAuthenticatedUserId() throws AuthenticationException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User)
            return ((User) principal).getId();
        throw new AuthenticationException("Impossible de récupérer l'identifiant de l'utilisateur connecté");
    }
}
