package br.com.azi.patrimoniomobiliario.domain.validation;

public interface GenericValidator<U> {
    void validate(U u);
}
