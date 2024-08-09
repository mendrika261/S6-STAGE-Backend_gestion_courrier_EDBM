package mg.edbm.mail.controller;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.service.MailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mails")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    /*@PostMapping
    @Secured({SecurityConfig.ROLE_ADMIN, SecurityConfig.ROLE_RECEPTIONIST})
    public ResponseEntity<MailDto> create(@Valid MailDto mailDto) {
        final MailDto createdMail = mailService.create(mailDto);
        return ResponseEntity.ok(createdMail);
    }*/
}
