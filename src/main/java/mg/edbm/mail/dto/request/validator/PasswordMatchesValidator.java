package mg.edbm.mail.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import mg.edbm.mail.dto.request.PasswordRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordRequest> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(@Valid PasswordRequest passwordRequest, ConstraintValidatorContext context) {
        if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}