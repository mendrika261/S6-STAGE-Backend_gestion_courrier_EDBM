package mg.edbm.mail.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.properties.FileUploadProperties;
import mg.edbm.mail.utils.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ValidFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    private final FileUploadProperties fileUploadProperties;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
    }

    private boolean isValidType(MultipartFile file) {
        for (String allowedType : fileUploadProperties.ALLOWED_FILE_TYPES) {
            if (allowedType.equals(file.getContentType())) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidSize(MultipartFile file) {
        return file.getSize() <= fileUploadProperties.MAX_FILE_SIZE;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        String error = null;
        if (file == null) error = "Le fichier est obligatoire.";
        else {
            if (!isValidSize(file))
                error = "Le fichier ne peut pas dépasser " + NumberUtils.octetToMo(fileUploadProperties.MAX_FILE_SIZE) + " Mo.";

            if (!isValidType(file))
                error = "Les types de fichier autorisé sont: " +
                        String.join(", ", fileUploadProperties.getAllowedFileTypesExtensions());
        }
        if (error != null) {
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
            return false;
        }
        return true;
    }
}
