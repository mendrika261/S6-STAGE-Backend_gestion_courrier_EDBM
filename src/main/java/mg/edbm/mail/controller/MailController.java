package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.FileDto;
import mg.edbm.mail.dto.request.MouvementRequest;
import mg.edbm.mail.dto.response.MouvementResponse;
import mg.edbm.mail.dto.request.FileUploadRequest;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.response.MailResponse;
import mg.edbm.mail.entity.File;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.service.MailService;
import mg.edbm.mail.service.MouvementService;
import mg.edbm.mail.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/mails")
@RequiredArgsConstructor
@Transactional
public class MailController {
    private final MailService mailService;
    private final UserService userService;
    private final MouvementService mouvementService;

    @GetMapping("/{mailId}/files")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<Page<FileDto>> listFiles(@PathVariable UUID mailId, @Valid ListRequest listRequest) throws NotFoundException {
        final Page<File> files = mailService.listFiles(mailId, listRequest);
        final Page<FileDto> mappedFileDtoList = files.map(FileDto::new);
        return ResponseEntity.ok(mappedFileDtoList);
    }

    // For the author of the mail only
    @PostMapping("/{mailId}/files")
    public ResponseEntity<FileDto> uploadFile(@PathVariable UUID mailId, @Valid FileUploadRequest fileUploadRequest)
            throws AuthenticationException, IOException {
        final File uploadedFile = mailService.uploadMailFile(mailId, fileUploadRequest, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new FileDto(uploadedFile));
    }

    // For the author of the mail only
    @DeleteMapping("/{mailId}/files/{fileId}")
    public ResponseEntity<MailResponse> deleteFile(@PathVariable UUID mailId, @PathVariable UUID fileId)
            throws NotFoundException, AuthenticationException {
        final Mail mail = mailService.deleteMailFileSendBy(mailId, fileId, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new MailResponse(mail));
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @GetMapping("/waiting")
    public ResponseEntity<Page<MailResponse>> listWaitingMails(@Valid ListRequest listRequest) {
        final Page<Mail> mails = mailService.listWaitingMails(listRequest);
        final Page<MailResponse> mappedMailResponseList = mails.map(MailResponse::new);
        return ResponseEntity.ok(mappedMailResponseList);
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @PostMapping("/{mailId}/mouvements")
    public ResponseEntity<MouvementResponse> createMouvement(@PathVariable UUID mailId)
            throws AuthenticationException, NotFoundException {
        final Mouvement mouvement = mouvementService.createMouvement(mailId, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new MouvementResponse(mouvement));
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @DeleteMapping("/{mailId}/mouvements")
    public ResponseEntity<MouvementResponse> removeMouvement(@PathVariable UUID mailId)
            throws NotFoundException, AuthenticationException {
        final Mouvement mouvement = mouvementService.removeLastMouvement(mailId, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new MouvementResponse(mouvement));
    }

    @Secured(SecurityConfig.ROLE_USER)
    @PatchMapping("/{mailId}/mouvements")
    public ResponseEntity<MailResponse> signMouvement(@PathVariable UUID mailId, LocalDateTime startDate)
            throws AuthenticationException {
        final Mail mail = mailService.signMouvementStart(mailId, startDate, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new MailResponse(mail));
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @GetMapping("/delivering")
    public ResponseEntity<Page<MailResponse>> listDeliveringMails(@Valid ListRequest listRequest) throws AuthenticationException {
        final Page<Mail> mails = mailService.listDeliveringMails(listRequest, userService.getAuthenticatedUser());
        final Page<MailResponse> mappedMailResponseList = mails.map(MailResponse::new);
        return ResponseEntity.ok(mappedMailResponseList);
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @PatchMapping("/{mailId}/mouvements/delivered")
    public ResponseEntity<MailResponse> deliverMail(@PathVariable UUID mailId, LocalDateTime endDate)
            throws AuthenticationException {
        final Mail mail = mailService.deliverMail(mailId, endDate, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new MailResponse(mail));
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @DeleteMapping("/{mailId}/mouvements/delivered")
    public ResponseEntity<MailResponse> cancelDelivery(@PathVariable UUID mailId)
            throws AuthenticationException {
        final Mail mail = mailService.cancelDelivery(mailId, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new MailResponse(mail));
    }

    @Secured(SecurityConfig.ROLE_MESSENGER)
    @PutMapping("/{mailId}/mouvements")
    public ResponseEntity<MouvementResponse> rerouteMouvement(@PathVariable UUID mailId,
                                                              @Valid MouvementRequest mouvementRequest)
            throws NotFoundException, AuthenticationException {
        final Mouvement mouvement = mailService.reroute(mailId, mouvementRequest, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new MouvementResponse(mouvement));
    }
}
