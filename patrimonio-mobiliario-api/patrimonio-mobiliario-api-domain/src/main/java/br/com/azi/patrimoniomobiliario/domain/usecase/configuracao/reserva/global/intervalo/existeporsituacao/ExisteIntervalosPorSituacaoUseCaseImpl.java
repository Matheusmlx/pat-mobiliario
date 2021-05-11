package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.converter.ExisteIntervalosPorSituacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class ExisteIntervalosPorSituacaoUseCaseImpl implements ExisteIntervalosPorSituacaoUseCase {

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ExisteIntervalosPorSituacaoOutputDataConverter outputDataConverter;

    @Override
    public ExisteIntervalosPorSituacaoOutputData executar(ExisteIntervalosPorSituacaoInputData inputData) {
        validarDadosEntrada(inputData);

        final boolean existeIntervalosPorSituacao = reservaIntervaloDataProvider.existe(inputData.getReservaId(),
            inputData.getSituacao());

        return outputDataConverter.to(inputData.getReservaId(), inputData.getSituacao(), existeIntervalosPorSituacao);
    }

    private void validarDadosEntrada(ExisteIntervalosPorSituacaoInputData inputData) {
        Validator.of(inputData)
            .validate(ExisteIntervalosPorSituacaoInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo.")
            .validate(ExisteIntervalosPorSituacaoInputData::getSituacao, Objects::nonNull, "Situação dos intervalos não foi informada.")
            .get();
    }
}
