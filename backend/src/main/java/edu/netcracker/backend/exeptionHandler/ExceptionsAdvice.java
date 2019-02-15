package edu.netcracker.backend.exeptionHandler;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dto.response.RequestExceptionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionsAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<RequestExceptionMessage>> handleException(MethodArgumentNotValidException ex){

        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        List<RequestExceptionMessage> requestExceptionMessages = new ArrayList<>();

        for (int i = 0; i < fieldErrors.size(); i++) {
            requestExceptionMessages.add(new RequestExceptionMessage(
                    111,
                    fieldErrors.get(i).getDefaultMessage() + "|||||" + fieldErrors.get(i).getField(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        }

        return  ResponseEntity.badRequest().body(requestExceptionMessages);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(BadCredentialsException ex){
        RequestExceptionMessage exceptionMessage = new RequestExceptionMessage(
                556,
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return  ResponseEntity.badRequest().body(exceptionMessage);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(RequestException ex, WebRequest request){
        RequestExceptionMessage exceptionMessage = new RequestExceptionMessage(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return ResponseEntity.badRequest().body(exceptionMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(AccessDeniedException ex, WebRequest request){
        RequestExceptionMessage exceptionMessage = new RequestExceptionMessage(
                4033,
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return ResponseEntity.badRequest().body(exceptionMessage);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(LockedException ex, WebRequest request){
        RequestExceptionMessage exceptionMessage = new RequestExceptionMessage(
                23,
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return ResponseEntity.badRequest().body(exceptionMessage);
    }
}
