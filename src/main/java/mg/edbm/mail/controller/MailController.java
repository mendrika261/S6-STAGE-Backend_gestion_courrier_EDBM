package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.FileDto;
import mg.edbm.mail.dto.request.FileUploadRequest;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.response.MailResponse;
import mg.edbm.mail.entity.File;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.service.FileService;
import mg.edbm.mail.service.MailService;
import mg.edbm.mail.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/mails")
@RequiredArgsConstructor
@Transactional
public class MailController {
    private final MailService mailService;
    private final UserService userService;

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
}
