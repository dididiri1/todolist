package sample.todolist.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.todolist.dto.ApiResponse;
import sample.todolist.handler.ex.JwtException;
import sample.todolist.handler.ex.ValidationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> validationException(ValidationException e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<?>> bindException(BindException e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> jwtException(JwtException e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }
}
