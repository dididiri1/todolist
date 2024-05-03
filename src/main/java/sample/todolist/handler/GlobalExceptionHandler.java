package sample.todolist.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.todolist.dto.ApiResponse;
import sample.todolist.handler.ex.validationException;

import javax.validation.ValidationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(validationException.class)
    public ResponseEntity<ApiResponse<?>> validationException(validationException e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<?>> bindException(BindException e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
