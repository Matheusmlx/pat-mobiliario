package br.com.azi.patrimoniomobiliario.domain.validation;

import lombok.Data;

import java.util.List;

@Data
public class GenericValidationException extends RuntimeException {
    List<String> args;

    public GenericValidationException(String message) {
        super(message);
    }
}
