package mg.edbm.gestion_courrier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class FormResponse {
    private String[] messagesGeneral;
    private HashMap<String, String[]> messagesChamps;

    public FormResponse(String... messagesGeneral) {
        this.messagesGeneral = messagesGeneral;
    }

    public static FormResponse extract(String[] generalMessages, BindingResult bindingResult) {
        final HashMap<String, String[]> validationResponses = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            validationResponses.put(fieldError.getField(), new String[]{fieldError.getDefaultMessage()});
        });

        return new FormResponse(generalMessages, validationResponses);
    }

    public static FormResponse extract(BindingResult bindingResult) {
        return extract(new String[0], bindingResult);
    }
}
