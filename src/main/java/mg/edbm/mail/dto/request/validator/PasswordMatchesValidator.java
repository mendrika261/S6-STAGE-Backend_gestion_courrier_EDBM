package mg.edbm.mail.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import mg.edbm.mail.dto.request.PasswordDtoRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordDtoRequest> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(@Valid PasswordDtoRequest passwordDtoRequest, ConstraintValidatorContext context) {
        if (!passwordDtoRequest.getPassword().equals(passwordDtoRequest.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}