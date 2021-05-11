package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception;

public class IncorporacaoEmProcessamentoException extends RuntimeException {
    public IncorporacaoEmProcessamentoException() {
        super("Não é possível adicionar documentos a uma incorporação em processamento.");
    }
}
