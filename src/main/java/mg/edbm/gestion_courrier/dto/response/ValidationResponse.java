package mg.edbm.gestion_courrier.dto.response;

import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Data
public class ValidationResponse {
    String champ;
    String[] messages;

    public static ValidationResponse[] liste(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    ValidationResponse validationResponse = new ValidationResponse();
                    validationResponse.setChamp(fieldError.getField());
                    validationResponse.setMessages(new String[]{fieldError.getDefaultMessage()});
                    return validationResponse;
                })
                .toArray(ValidationResponse[]::new);
    }
}
