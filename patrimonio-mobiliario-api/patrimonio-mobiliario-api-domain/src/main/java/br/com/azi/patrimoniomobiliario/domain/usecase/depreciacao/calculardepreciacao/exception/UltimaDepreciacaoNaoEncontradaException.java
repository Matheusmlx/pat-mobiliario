package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception;

public class UltimaDepreciacaoNaoEncontradaException extends RuntimeException {
    public UltimaDepreciacaoNaoEncontradaException(Long patrimonioId) {
        super("Erro ao buscar a última depreciação do patrimônio #" + patrimonioId);
    }
}
