package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.FileDto;
import mg.edbm.mail.dto.request.FileUploadRequest;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.entity.File;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.security.Self;
import mg.edbm.mail.service.FileService;
import mg.edbm.mail.service.MailService;
import mg.edbm.mail.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    private final FileService fileService;
    private final UserService userService;

    @GetMapping("/{mailId}/files")
    public ResponseEntity<Page<FileDto>> listFiles(@PathVariable UUID mailId, @Valid ListRequest listRequest) throws NotFoundException {
        final Page<File> files = mailService.listFiles(mailId, listRequest);
        final Page<FileDto> mappedFileDtoList = files.map(FileDto::new);
        return ResponseEntity.ok(mappedFileDtoList);
    }

    @PostMapping("/{mailId}/files")
    public ResponseEntity<FileDto> uploadFile(@PathVariable UUID mailId, @Valid FileUploadRequest fileUploadRequest)
            throws AuthenticationException, IOException {
        final File uploadedFile = fileService.uploadMailFile(mailId, fileUploadRequest, userService.getAuthenticatedUser());
        return ResponseEntity.ok(new FileDto(uploadedFile));
    }
}
