package mg.edbm.mail.dto.request.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReceiverUserOrNameAndLocationValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReceiverUserOrNameAndLocation {
    String message() default "Le destinataire doit être un utilisateur ou un nom et une localisation";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
