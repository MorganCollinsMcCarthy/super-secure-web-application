package app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CentreNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CentreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String centreNotFoundHandler(CentreNotFoundException ex) {
        return ex.getMessage();
    }
}
