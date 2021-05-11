package br.com.azi.patrimoniomobiliario.entrypoint;

import br.com.azi.patrimoniomobiliario.domain.validation.ExceptionList;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ExceptionList.class})
    public ResponseEntity<Object> handleExceptionList(ExceptionList ex) {
        List<ErrorMessage> response = ex.getExceptions().stream().map(e -> new ErrorMessage(e.getClass().getName(), e.getMessage(), e.getArgs())).collect(Collectors.toList());
        return new ResponseEntity<>(new Encapsulator(response), HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    static class ErrorMessage {
        String name;
        String message;
        List<String> args;
    }

    @Data
    @AllArgsConstructor
    static class Encapsulator{
        List<ErrorMessage> errorMessages;
    }
}
