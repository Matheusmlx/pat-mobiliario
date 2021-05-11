package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception;

public class EmpenhoNaoExisteException extends RuntimeException {
    public EmpenhoNaoExisteException() {
        super("Empenho não existe");
    }
}
