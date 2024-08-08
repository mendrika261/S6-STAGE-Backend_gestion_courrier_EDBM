package mg.edbm.mail.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.utils.StringCustomUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String templateName, Context context) {
        final String message = templateEngine.process(templateName, context);
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            mimeMessage.setContent(message, "text/html; charset=UTF-8");
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
            log.info("Sending email to {}", to);
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error while sending email to {}", to, e);
        }
    }

    public void sendNewUserEmail(User user) {
        final String templateName = "new-user-email";
        final Context context = new Context();
        context.setVariable("fullName", StringCustomUtils.titleCase(user.getFullName()));
        context.setVariable("email", user.getEmail());
        context.setVariable("password", user.getPassword());
        sendEmail(user.getEmail(), "Bienvenue sur EDBM Courrier", templateName, context);
        log.info("New user email sent to {}", user.getEmail());
    }

}
