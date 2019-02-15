package edu.netcracker.backend.exeptionHandler;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dto.response.RequestExceptionMessage;
import edu.netcracker.backend.dto.response.exeptionResponce.ExceptionMessage;
import edu.netcracker.backend.dto.response.exeptionResponce.MethodArgumentNotValidExceptionMessage;
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

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ExceptionsAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(MethodArgumentNotValidException ex){

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        RequestExceptionMessage<MethodArgumentNotValidExceptionMessage> requestExceptionMessages =
                new RequestExceptionMessage<>();
        requestExceptionMessages.setCode(-1);
        requestExceptionMessages.setCurrentTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<MethodArgumentNotValidExceptionMessage> exceptionMessages = new ArrayList<>();

        fieldErrors.forEach(fieldError -> exceptionMessages.
                add(new MethodArgumentNotValidExceptionMessage(fieldError.getField(), fieldError.getDefaultMessage())));

        requestExceptionMessages.setMessages(exceptionMessages);

        return  ResponseEntity.badRequest().body(requestExceptionMessages);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(BadCredentialsException ex){

        return  ResponseEntity.badRequest().body(requestExceptionMessage(ex));
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(RequestException ex, WebRequest request){
        return  ResponseEntity.badRequest().body(requestExceptionMessage(ex));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(AccessDeniedException ex, WebRequest request){
        return  ResponseEntity.badRequest().body(requestExceptionMessage(ex));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(LockedException ex, WebRequest request){
        return  ResponseEntity.badRequest().body(requestExceptionMessage(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RequestExceptionMessage> handleException(Exception ex, WebRequest request){
        RequestExceptionMessage<ExceptionMessage> exceptionMessage = new RequestExceptionMessage<>();
        exceptionMessage.setCode(-2);
        exceptionMessage.setCurrentTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        exceptionMessage.setMessages(Collections.singletonList(new ExceptionMessage("Something went wrong")));

        return ResponseEntity.badRequest().body(exceptionMessage);
    }

    private <T> RequestExceptionMessage requestExceptionMessage(Exception ex) {
        RequestExceptionMessage<ExceptionMessage> exceptionMessage = new RequestExceptionMessage<>();
        exceptionMessage.setCode(-2);
        exceptionMessage.setCurrentTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        exceptionMessage.setMessages(Collections.singletonList(new ExceptionMessage(ex.getMessage())));

        return exceptionMessage;
    }
}
