package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.exception;

public class FiltroIncompletoException extends RuntimeException {
    public FiltroIncompletoException(String mensagem) {
        super(mensagem);
    }
}
