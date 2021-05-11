package br.com.azi.patrimoniomobiliario.domain.validation;

import lombok.Data;

import java.util.List;

@Data
public class ExceptionList extends RuntimeException {
    private List<GenericValidationException> exceptions;

    public ExceptionList(String message, List<GenericValidationException> exceptions) {
        super(message);
        this.exceptions = exceptions;
    }
}
