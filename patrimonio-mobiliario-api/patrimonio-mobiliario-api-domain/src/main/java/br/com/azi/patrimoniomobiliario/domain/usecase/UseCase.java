package br.com.azi.patrimoniomobiliario.domain.usecase;

public interface UseCase<INPUT, OUTPUT> {
    OUTPUT executar(INPUT inputData);
}
