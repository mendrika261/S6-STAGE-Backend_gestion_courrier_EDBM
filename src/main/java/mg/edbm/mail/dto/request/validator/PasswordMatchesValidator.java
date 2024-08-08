package mg.edbm.mail.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import mg.edbm.mail.dto.request.UserDtoRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDtoRequest> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(@Valid UserDtoRequest userDtoRequest, ConstraintValidatorContext context) {
        /*final String password = userDtoRequest.getPassword() == null ? "" : userDtoRequest.getPassword();
        final String confirmPassword = userDtoRequest.getConfirmPassword() == null ? "" : userDtoRequest.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }*/
        return true;
    }
}