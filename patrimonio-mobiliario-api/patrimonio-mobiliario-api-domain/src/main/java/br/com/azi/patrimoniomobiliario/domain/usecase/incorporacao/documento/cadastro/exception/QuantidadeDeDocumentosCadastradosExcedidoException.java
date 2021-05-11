package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception;

public class QuantidadeDeDocumentosCadastradosExcedidoException extends RuntimeException {
    public QuantidadeDeDocumentosCadastradosExcedidoException() {
        super("A quantidade máxima de documentos cadastrados para esta incorporação foi atingida.");
    }
}
