package mg.edbm.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.mail.exception.ValidationException;
import org.springframework.validation.BindingResult;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class FormResponse {
    private String[] globalMessages;
    private HashMap<String, String[]> fieldsMessages = new HashMap<>();

    public FormResponse(String... globalMessages) {
        setGlobalMessages(globalMessages);
    }

    public FormResponse(ValidationException validationException) {
        setGlobalMessages(validationException.getMessage());
        for (final String field : validationException.getFields()) {
            getFieldsMessages().put(field, new String[]{validationException.getMessage()});
        }
    }

    public static FormResponse extractFieldsErrors(BindingResult bindingResult, String... globalMessages) {
        final HashMap<String, String[]> validationResponses = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError ->
                validationResponses.put(fieldError.getField(), new String[]{fieldError.getDefaultMessage()}));

        return new FormResponse(globalMessages, validationResponses);
    }

    public void setGlobalMessages(String... globalMessages) {
        this.globalMessages = globalMessages;
    }
}
