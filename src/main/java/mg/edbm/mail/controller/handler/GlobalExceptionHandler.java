package mg.edbm.mail.controller.handler;


import mg.edbm.mail.dto.response.FormResponse;
import mg.edbm.mail.dto.response.MessageResponse;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FormResponse> handleMethodArgumentNotValidExceptions(
            MethodArgumentNotValidException ex) {
        final FormResponse formResponse = FormResponse.extractFieldsErrors(ex.getBindingResult());
        return ResponseEntity.badRequest().body(formResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<FormResponse> handleAuthentificationExceptions(AuthenticationException ex) {
        final FormResponse formResponse = new FormResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(formResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageResponse> handleAccessDeniedExceptions() {
        final MessageResponse messageResponse = new MessageResponse("403: Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MessageResponse> handleHttpRequestMethodNotSupportedExceptions() {
        final MessageResponse messageResponse = new MessageResponse("405: Method not allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(messageResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<FormResponse> handleValidationExceptions(ValidationException ex) {
        final FormResponse formResponse = new FormResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(formResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException e) {
        if(e.getParameter().getParameterType() == UUID.class) return handleNoSuchElementExceptions(e);
        final MessageResponse messageResponse = new MessageResponse("Veuillez vérifier les paramètres de la requête, " +
                "les valeurs fournies ne sont pas celles attendues");
        return ResponseEntity.badRequest().body(messageResponse);
    }

    @ExceptionHandler({NotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<MessageResponse> handleNoSuchElementExceptions(Exception e) {
        final MessageResponse messageResponse = new MessageResponse("404: Not found - " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
    }
}
