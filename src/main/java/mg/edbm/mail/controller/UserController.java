package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.DatabaseConfig;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.AdminStatsDto;
import mg.edbm.mail.dto.MessengerStatsDto;
import mg.edbm.mail.dto.request.*;
import mg.edbm.mail.dto.response.MailResponse;
import mg.edbm.mail.dto.request.type.MailType;
import mg.edbm.mail.dto.response.MouvementResponse;
import mg.edbm.mail.dto.response.UserResponse;
import mg.edbm.mail.dto.response.FormResponse;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.UserStatus;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.exception.ValidationException;
import mg.edbm.mail.security.AdminOrSelf;
import mg.edbm.mail.security.Self;
import mg.edbm.mail.service.MailService;
import mg.edbm.mail.service.MouvementService;
import mg.edbm.mail.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Transactional
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final MouvementService mouvementService;

    @PostMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<UserResponse> create(@Valid UserRequest userRequest)
            throws AuthenticationException, ValidationException, NotFoundException {
        final User user = userService.create(userRequest, userService.getAuthenticatedUser());
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }

    @GetMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<Page<UserResponse>> list(@Valid ListRequest listRequest) {
        final Page<User> users = userService.list(listRequest);
        final Page<UserResponse> mappedUserDtoList = users.map(UserResponse::new);
        return ResponseEntity.ok(mappedUserDtoList);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<UserResponse>> listActiveReceiver(@Valid ListRequest listRequest) throws AuthenticationException {
        final Page<User> users = userService.listActiveReceiver(listRequest, userService.getAuthenticatedUser());
        final Page<UserResponse> mappedUserDtoList = users.map(UserResponse::new);
        return ResponseEntity.ok(mappedUserDtoList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> get(@PathVariable UUID userId) {
        final User user = userService.getUser(userId);
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }

    @GetMapping("/{userId}/password")
    @AdminOrSelf
    public ResponseEntity<UserResponse> resetPassword(@PathVariable UUID userId) throws NotFoundException, ValidationException {
        final User user = userService.resetPasswordByUserId(userId);
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }

    @PutMapping("/{userId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<UserResponse> update(@PathVariable UUID userId, @Valid UserRequest userRequest)
            throws NotFoundException, AuthenticationException {
        final User user = userService.updateWithoutPassword(userId, userRequest, userService.getAuthenticatedUser());
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }

    @PutMapping("/{userId}/profile")
    @Self
    public ResponseEntity<UserResponse> update(@PathVariable UUID userId, @Valid UserProfileRequest userProfileRequest)
            throws NotFoundException, AuthenticationException {
        final User user = userService.updateWithoutPassword(userId, userProfileRequest, userService.getAuthenticatedUser());
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }

    @PutMapping("/{userId}/password")
    @Self
    public ResponseEntity<UserResponse> updatePassword(@PathVariable UUID userId, @Valid PasswordRequest passwordRequest)
            throws NotFoundException, AuthenticationException, ValidationException {
        final User user = userService.updatePassword(userId, passwordRequest, userService.getAuthenticatedUser());
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }

    @PutMapping("/{userId}/status")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<UserResponse> updateStatus(@PathVariable UUID userId, UserStatus status)
            throws NotFoundException, AuthenticationException, ValidationException {
        final User user = userService.updateStatus(userId, status, userService.getAuthenticatedUser());
        final UserResponse mappedUserResponse = new UserResponse(user);
        return ResponseEntity.ok(mappedUserResponse);
    }

    @GetMapping("{userId}/mails")
    @AdminOrSelf
    public ResponseEntity<Page<MailResponse>> getMailByUser(@PathVariable UUID userId, @Valid MailType type, @Valid ListRequest listRequest) {
        final Page<Mail> mails = mailService.getMailsByUser(userId, type, listRequest);
        final Page<MailResponse> mappedMailDtoList = mails.map(MailResponse::new);
        return ResponseEntity.ok(mappedMailDtoList);
    }

    @Self
    @PostMapping("{userId}/mails")
    @Secured({SecurityConfig.ROLE_USER, SecurityConfig.ROLE_RECEPTIONIST})
    public ResponseEntity<MailResponse> createOutgoingMail(@PathVariable UUID userId, @Valid MailRequest mailRequest)
            throws AuthenticationException, NotFoundException {
        final Mail createdMail = mailService.createMail(userId, mailRequest, userService.getAuthenticatedUser());
        final MailResponse mappedMailResponse = new MailResponse(createdMail);
        return ResponseEntity.ok(mappedMailResponse);
    }

    @GetMapping("{userId}/mails/{mailId}")
    @AdminOrSelf
    public ResponseEntity<MailResponse> getMailByUser(@PathVariable UUID userId, @PathVariable UUID mailId)
            throws NotFoundException {
        final Mail mail = mailService.getMailByUser(userId, mailId);
        final MailResponse mappedMailResponse = new MailResponse(mail);
        return ResponseEntity.ok(mappedMailResponse);
    }

    @Self
    @PutMapping("{userId}/mails/{mailId}")
    @Secured({SecurityConfig.ROLE_USER, SecurityConfig.ROLE_RECEPTIONIST})
    public ResponseEntity<MailResponse> updateOutgoingMail(@PathVariable UUID userId,
                                                           @PathVariable UUID mailId,
                                                           @Valid MailRequest mailRequest)
            throws AuthenticationException, NotFoundException {
        final Mail updatedMail = mailService.updateMail(userId, mailId, mailRequest, userService.getAuthenticatedUser());
        final MailResponse mappedMailResponse = new MailResponse(updatedMail);
        return ResponseEntity.ok(mappedMailResponse);
    }

    @PatchMapping("{userId}/mails/{mailId}/status")
    @Secured({SecurityConfig.ROLE_USER, SecurityConfig.ROLE_RECEPTIONIST})
    @Self
    public ResponseEntity<MailResponse> updateMailStatus(@PathVariable UUID userId,
                                                         @PathVariable UUID mailId,
                                                         MailStatus mailStatus)
            throws AuthenticationException, NotFoundException {
        final Mail updatedMail = mailService.updateMailStatus(userId, mailId, mailStatus, userService.getAuthenticatedUser());
        final MailResponse mappedMailResponse = new MailResponse(updatedMail);
        return ResponseEntity.ok(mappedMailResponse);
    }

    @Self
    @DeleteMapping("{userId}/mails/{mailId}")
    @Secured({SecurityConfig.ROLE_USER, SecurityConfig.ROLE_RECEPTIONIST})
    public ResponseEntity<MailResponse> deleteMail(@PathVariable UUID userId, @PathVariable UUID mailId)
            throws NotFoundException {
        final Mail deletedMail = mailService.deleteMail(userId, mailId);
        final MailResponse mappedMailResponse = new MailResponse(deletedMail);
        return ResponseEntity.ok(mappedMailResponse);
    }

    @Self
    @GetMapping("/{userId}/mails/transit")
    @Secured({SecurityConfig.ROLE_USER, SecurityConfig.ROLE_MESSENGER, SecurityConfig.ROLE_RECEPTIONIST})
    public ResponseEntity<Page<MouvementResponse>> listTransitMails(@PathVariable UUID userId, @Valid ListRequest listRequest) {
        final Page<Mouvement> mouvements = mouvementService.listTransitMails(listRequest, userId);
        final Page<MouvementResponse> mappedMouvementResponseList = mouvements.map(MouvementResponse::new);
        return ResponseEntity.ok(mappedMouvementResponseList);
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @GetMapping("/messenger/stats")
    public ResponseEntity<MessengerStatsDto> getMessengerStats() throws AuthenticationException {
        final MessengerStatsDto messengerStatsDto = mouvementService.getMessengerStats(userService.getAuthenticatedUser());
        return ResponseEntity.ok(messengerStatsDto);
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @GetMapping("/messenger/mails")
    public ResponseEntity<Page<MouvementResponse>> getMessengerDeliveringMail(ListRequest listRequest) throws AuthenticationException {
        final Page<Mouvement> mouvements = mouvementService.getMessengerDeliveringMail(listRequest, userService.getAuthenticatedUser());
        return ResponseEntity.ok(mouvements.map(MouvementResponse::new));
    }

    @Secured(SecurityConfig.ROLE_ADMIN)
    @GetMapping("/admin/stats")
    public ResponseEntity<AdminStatsDto> getAdminStats() {
        final AdminStatsDto adminStats = mouvementService.getAdminStats();
        return ResponseEntity.ok(adminStats);
    }

    @Secured(SecurityConfig.ROLE_ADMIN)
    @GetMapping("/messengers/free")
    public ResponseEntity<List<UserResponse>> listFreeMessengers() {
        final List<User> users = userService.listFreeMessengers();
        return ResponseEntity.ok(users.stream().map(UserResponse::new).collect(Collectors.toList()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FormResponse> handleDataIntegrityException(DataIntegrityViolationException ex) {
        final FormResponse formResponse = new FormResponse();
        if(ex.getMessage().contains(DatabaseConfig.UNIQUE_ERROR_CONSTRAINT))
            formResponse.addFieldErrors("email", "Cette adresse email est déjà utilisée");
        return ResponseEntity.badRequest().body(formResponse);
    }
}
