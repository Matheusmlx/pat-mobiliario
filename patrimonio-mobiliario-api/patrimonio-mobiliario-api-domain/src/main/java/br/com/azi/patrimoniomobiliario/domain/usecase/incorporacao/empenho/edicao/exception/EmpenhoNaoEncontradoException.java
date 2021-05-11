package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception;

public class EmpenhoNaoEncontradoException extends RuntimeException {
    public EmpenhoNaoEncontradoException() {
        super("Empenho não encontrado");
    }
}
