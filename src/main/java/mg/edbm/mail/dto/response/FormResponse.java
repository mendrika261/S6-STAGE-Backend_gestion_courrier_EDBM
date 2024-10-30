package mg.edbm.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.*;

@Data
@AllArgsConstructor
public class FormResponse {
    private List<String> globalMessages;
    private HashMap<String, List<String>> fieldsMessages = new HashMap<>();

    public FormResponse(String... globalMessages) {
        setGlobalMessages(List.of(globalMessages));
    }

    public static FormResponse extractFieldsErrors(BindingResult bindingResult, String... globalMessages) {
        final List<String> combinedGlobalMessages = new ArrayList<>();
        if (globalMessages != null)
            combinedGlobalMessages.addAll(Arrays.asList(globalMessages));
        bindingResult.getGlobalErrors().forEach(globalError ->
                combinedGlobalMessages.add(globalError.getDefaultMessage()));

        final HashMap<String, List<String>> validationResponses = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError ->
                validationResponses.put(fieldError.getField(),
                        List.of(Objects.requireNonNull(fieldError.getDefaultMessage()))));

        return new FormResponse(combinedGlobalMessages, validationResponses);
    }

    public void addFieldErrors(String field, String... message) {
        getFieldsMessages().put(field, List.of(message));
    }
}
