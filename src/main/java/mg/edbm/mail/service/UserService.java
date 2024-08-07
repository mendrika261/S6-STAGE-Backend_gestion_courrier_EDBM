package mg.edbm.mail.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.repository.UserRepository;
import mg.edbm.mail.utils.UserUtils;
import mg.edbm.mail.entity.type.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final SessionService sessionService;

    public User verifyUser(String email, String password, HttpServletRequest request) throws AuthenticationException {
        final Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            final User authenticatedUser = user.get();
            if(authenticatedUser.isActive()) {
                final boolean isPasswordValid = passwordEncoder.matches(password, authenticatedUser.getPassword());
                if(isPasswordValid)
                    return authenticatedUser;
                sessionService.createTentativeSession(authenticatedUser, request);
                throw new AuthenticationException("Votre mot de passe est incorrect");
            } else {
                throw new AuthenticationException("Votre compte est désactivé");
            }
        }
        throw new AuthenticationException("Le compte n'existe pas");
    }

    public Token authenticateWithPassword(String email, String password, HttpServletRequest request)
            throws AuthenticationException {
        final User user = verifyUser(email, password, request);
        return tokenService.generateToken(request, user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getAuthenticatedUser() {
        return getUser(UserUtils.getAuthenticatedUserId());
    }
}
