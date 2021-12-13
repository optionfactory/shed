package net.optionfactory.minispring.config.api;

import net.optionfactory.minispring.minify.api.v1.MappingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyErrors handleInvalidRequest(Exception ex) {
        final MyErrors errors = new MyErrors();
        errors.message = ex.getMessage();
        return errors;
    }

    public static class MyFieldError {

        public String fieldName;
        public String message;

        public MyFieldError(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }
    }

    public static class MyErrors {

        public String message;
        public List<MyFieldError> fieldErrors;
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyErrors handleInvalidRequest(ConstraintViolationException ex) {
        final MyErrors errors = new MyErrors();
        final String globalErrors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(". "));
        errors.message = "Validation failed. " + globalErrors;
        errors.fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(fe -> new MyFieldError(fe.getPropertyPath().toString(), fe.getMessage()))
                .collect(Collectors.toList());
        return errors;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyErrors handleInvalidRequest(MethodArgumentNotValidException ex) {
        final MyErrors errors = new MyErrors();
        final String globalErrors = String.join(". ", ex.getBindingResult().getGlobalErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
        errors.message = "Validation failed. " + globalErrors;
        errors.fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> new MyFieldError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return errors;
    }

}
