package mg.edbm.mail.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.PasswordRequest;
import mg.edbm.mail.dto.request.UserRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.UserStatus;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.exception.ValidationException;
import mg.edbm.mail.repository.UserRepository;
import mg.edbm.mail.utils.UserUtils;
import mg.edbm.mail.entity.type.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final SessionService sessionService;
    private final RoleService roleService;
    private final EmailService emailService;
    private final LocationService locationService;

    public User verifyUser(String email, String password, HttpServletRequest request) throws AuthenticationException {
        final Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            final User authenticatedUser = user.get();
            if(authenticatedUser.isActive() || authenticatedUser.getStatus().equals(UserStatus.PENDING)) {
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

    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getAuthenticatedUser() throws AuthenticationException {
        return getUser(UserUtils.getAuthenticatedUserId());
    }

    public Page<User> list(ListRequest listRequest) {
        final Pageable pageable = listRequest.toPageable();
        final Specification<User> specification = new SpecificationImpl<>(listRequest);
        return userRepository.findAll(specification, pageable);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    public String generateSecurityCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private User initializeUserCreation(UserRequest userRequest, User authenticatedUser) throws NotFoundException {
        User user = new User(userRequest, authenticatedUser);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoles(roleService.getRolesFromCodes(userRequest.getRoles()));
        user.setLocation(locationService.get(userRequest.getLocationId()));
        return save(user);
    }

    private User resetPassword(User user, Boolean isNewUser) throws ValidationException {
        final String securityCode = generateSecurityCode();
        user.setPassword(securityCode);
        user.setStatus(UserStatus.PENDING);
        emailService.sendResetPasswordEmail(user, isNewUser);

        final String hashedPassword = passwordEncoder.encode(securityCode);
        user.setPassword(hashedPassword);
        log.info("Password reset for {}", user);
        return save(user);
    }

    public User resetPasswordByUserId(UUID userId) throws NotFoundException, ValidationException {
        final User user = get(userId);
        final Boolean isNewUser = user.getStatus() == UserStatus.CREATED;
        return resetPassword(user, isNewUser);
    }

    public User resetPasswordByEmail(String email) throws NotFoundException, ValidationException {
        final User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("L'utilisateur avec l'adresse email " + email + " n'existe pas")
        );
        return resetPassword(user, false);
    }

    public User create(UserRequest userRequest, User authenticatedUser) throws ValidationException, NotFoundException {
        User user = initializeUserCreation(userRequest, authenticatedUser);
        if(userRequest.getPasswordGenerated()) resetPassword(user, true);
        log.info("{} created {}", authenticatedUser, user);
        return user;
    }

    public User get(UUID uuid) throws NotFoundException {
        return userRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException("L'utilisateur #" + uuid + " n'existe pas")
        );
    }

    private User update(User user, User authenticatedUser) {
        log.info("{} updated {}", authenticatedUser, user);
        return save(user);
    }

    public User updateWithoutPassword(UUID userId, UserRequest userRequest, User authenticatedUser)
            throws NotFoundException {
        final User user = get(userId);
        user.updateWithoutPassword(userRequest, authenticatedUser);
        user.setRoles(roleService.getRolesFromCodes(userRequest.getRoles()));
        user.setLocation(locationService.get(userRequest.getLocationId()));
        return update(user, authenticatedUser);
    }

    public User updatePassword(UUID userId, PasswordRequest passwordRequest, User authenticatedUser)
            throws NotFoundException, ValidationException {
        final User user = get(userId);
        final String hashedPassword = passwordEncoder.encode(passwordRequest.getPassword());
        user.updatePassword(hashedPassword, authenticatedUser);
        update(user, authenticatedUser);
        return updateStatus(userId, UserStatus.ACTIVE, authenticatedUser);
    }

    public User updateStatus(UUID userId, UserStatus status, User authenticatedUser)
            throws NotFoundException, ValidationException {
        if(status == null) throw new ValidationException("Le statut ne peut pas être vide");
        final User user = get(userId);
        user.setStatus(status);
        return update(user, authenticatedUser);
    }
}
