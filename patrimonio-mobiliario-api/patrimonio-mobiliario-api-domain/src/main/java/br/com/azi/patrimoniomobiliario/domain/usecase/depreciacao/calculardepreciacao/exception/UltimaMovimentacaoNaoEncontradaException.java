package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception;

import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.time.LocalDateTime;

public class UltimaMovimentacaoNaoEncontradaException extends RuntimeException {
    public UltimaMovimentacaoNaoEncontradaException(Long patrimonioId, LocalDateTime dataFinal) {
        super("Erro ao recuperar a última movimentação para o patrimônio #" + patrimonioId + " com data final " +
            DateValidate.formatarData(dataFinal));
    }
}
