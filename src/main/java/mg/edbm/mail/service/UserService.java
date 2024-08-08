package mg.edbm.mail.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.UserDtoRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.UserStatus;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.repository.UserRepository;
import mg.edbm.mail.utils.UserUtils;
import mg.edbm.mail.entity.type.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final SessionService sessionService;
    private final RoleService roleService;
    private final EmailService emailService;

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

    public User getAuthenticatedUser() throws AuthenticationException {
        return getUser(UserUtils.getAuthenticatedUserId());
    }

    public Page<User> list(ListRequest listRequest) {
        final Pageable pageable = listRequest.toPageable();
        final Specification<User> specification = new SpecificationImpl<>(listRequest.getCriteria());
        return userRepository.findAll(specification, pageable);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public String generatePassword() {
        final String password = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        log.info("Generated password: {}", password);
        return password;
    }

    private User initializeUserCreation(UserDtoRequest userDtoRequest, String plainPassword, User authenticatedUser) {
        final String encryptedPassword = passwordEncoder.encode(plainPassword);
        User user = new User(userDtoRequest, authenticatedUser);
        user.setPassword(encryptedPassword);
        user.setRoles(roleService.getRolesFromId(userDtoRequest.getRoles()));
        return save(user);
    }

    private void notifyUserCreation(User user, String plainPassword) {
        user.setPassword(plainPassword);
        emailService.sendNewUserEmail(user);
        user.setStatus(UserStatus.PENDING);
        save(user);
    }

    public User create(UserDtoRequest userDtoRequest, User authenticatedUser) {
        final String plainPassword = generatePassword();
        User user = initializeUserCreation(userDtoRequest, plainPassword, authenticatedUser);
        notifyUserCreation(user, plainPassword);
        log.info("{} created {}", authenticatedUser, user);
        return user;
    }
}
