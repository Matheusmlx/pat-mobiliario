package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception;

public class DocumentoNaoPertenceAIncorporacaoInformadaException extends RuntimeException {

    public DocumentoNaoPertenceAIncorporacaoInformadaException() {
        super("Este documento não pertence a incorporação informada");
    }
}
