package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception;

public class IncorporacaoEmProcessamentoException extends RuntimeException {
    public IncorporacaoEmProcessamentoException() {
        super("Não é possível remover documentos de uma incorporação em processamento.");
    }
}
