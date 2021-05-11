package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.exception;

public class IncorporacaoEmProcessamentoException extends RuntimeException {
    public IncorporacaoEmProcessamentoException() {
        super("Não é possível editar documentos de uma incorporação em processamento.");
    }
}
