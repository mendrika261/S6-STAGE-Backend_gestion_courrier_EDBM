package mg.edbm.mail.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mg.edbm.mail.dto.request.MailRequest;

public class ReceiverUserOrNameAndLocationValidator  implements ConstraintValidator<ReceiverUserOrNameAndLocation, MailRequest> {
    @Override
    public void initialize(ReceiverUserOrNameAndLocation constraintAnnotation) {
    }

    @Override
    public boolean isValid(MailRequest mailRequest, ConstraintValidatorContext constraintValidatorContext) {
        final boolean isSystemUser = mailRequest.getReceiverUserId() != null;
        final boolean isOtherUser = mailRequest.getReceiver() != null && mailRequest.getReceiverLocationId() != null;
        constraintValidatorContext.disableDefaultConstraintViolation();
        if ((!isSystemUser && !isOtherUser) || (isSystemUser && isOtherUser)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Le destinataire doit être un utilisateur ou un nom et une localisation")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
