package br.com.azi.patrimoniomobiliario.utils.converter;

public class NoArgsConstructorNotFoundException extends RuntimeException {
    public NoArgsConstructorNotFoundException() {
        super("No arguments constructor is required.");
    }
}
