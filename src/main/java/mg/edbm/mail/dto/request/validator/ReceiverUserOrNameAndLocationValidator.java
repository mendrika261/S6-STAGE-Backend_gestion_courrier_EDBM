package mg.edbm.mail.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mg.edbm.mail.dto.request.MailOutgoingRequest;

public class ReceiverUserOrNameAndLocationValidator  implements ConstraintValidator<ReceiverUserOrNameAndLocation, MailOutgoingRequest> {
    @Override
    public void initialize(ReceiverUserOrNameAndLocation constraintAnnotation) {
    }

    @Override
    public boolean isValid(MailOutgoingRequest mailOutgoingRequest, ConstraintValidatorContext constraintValidatorContext) {
        final boolean isSystemUser = mailOutgoingRequest.getReceiverUserId() != null;
        final boolean isOtherUser = mailOutgoingRequest.getReceiver() != null && mailOutgoingRequest.getReceiverLocationId() != null;
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
