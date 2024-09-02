package mg.edbm.mail.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.ValidationException;
import mg.edbm.mail.utils.StringCustomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Value("${app.url}")
    private String appUrl;
    @Value("${app.support.email}")
    private String supportEmail;

    @Async
    public void sendEmail(String to, String subject, String templateName, Context context) throws ValidationException {
        context.setVariable("appUrl", appUrl);
        context.setVariable("supportEmail", supportEmail);
        final String message = templateEngine.process(templateName, context);
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            mimeMessage.setFrom(new InternetAddress(from, "EDBM Courrier"));
            mimeMessage.setContent(message, "text/html; charset=UTF-8");
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
            log.info("Sending email to {}", to);
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error while sending email to {}", to, e);
            throw new ValidationException("Veuillez réessayer plus tard ou contacter directement le service informatique");
        }
    }

    @Async
    public void sendResetPasswordEmail(User user, Boolean isNewUser) throws ValidationException {
        final String templateName = isNewUser ? "new-user-email" : "reset-password-email";
        final Context context = new Context();
        context.setVariable("fullName", StringCustomUtils.titleCase(user.getFullName()));
        context.setVariable("email", user.getEmail());
        context.setVariable("password", user.getPassword());
        sendEmail(user.getEmail(), "Vos identifiants EDBM Courrier", templateName, context);
        log.info("Password reset email sent to {}", user.getEmail());
    }

}
