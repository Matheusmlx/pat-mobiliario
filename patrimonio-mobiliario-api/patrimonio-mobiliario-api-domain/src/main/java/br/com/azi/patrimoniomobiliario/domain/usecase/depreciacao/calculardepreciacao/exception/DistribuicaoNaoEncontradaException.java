package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception;

import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.time.LocalDateTime;

public class DistribuicaoNaoEncontradaException extends RuntimeException {
    public DistribuicaoNaoEncontradaException(Long patrimonioId, LocalDateTime dataInicio, LocalDateTime dataFinal) {
        super("Distribuição não encontrada. Patrimônio #" + patrimonioId
            + " Período: " + DateValidate.formatarData(dataInicio) + " - " + DateValidate.formatarData(dataFinal));
    }
}
