package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.converter.VerificarReservaPossuiNumerosUtilizadosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class VerificarReservaPossuiNumerosUtilizadosUseCaseImpl implements VerificarReservaPossuiNumerosUtilizadosUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    private final VerificarReservaPossuiNumerosUtilizadosOutputDataConverter converter;

    @Override
    public VerificarReservaPossuiNumerosUtilizadosOutputData executar(VerificarReservaPossuiNumerosUtilizadosInputData inputData) {
        validarDadosEntrada(inputData);

        Reserva reserva = buscar(inputData);
        List<ReservaIntervalo> intervalos = buscarIntervalosFinalizados(reserva);
        boolean possuiNumerosUtilizados = verificarPossuiNumerosUtilizados(intervalos);

        return converter.to(possuiNumerosUtilizados);
    }

    private void validarDadosEntrada(VerificarReservaPossuiNumerosUtilizadosInputData inputData) {
        Validator.of(inputData)
            .validate(VerificarReservaPossuiNumerosUtilizadosInputData::getId, Objects::nonNull, "Id da reserva é nulo")
            .get();
    }

    private Reserva buscar(VerificarReservaPossuiNumerosUtilizadosInputData inputData) {
        return reservaDataProvider.buscarPorId(inputData.getId())
            .orElseThrow(ReservaNaoEncontradaException::new);
    }

    private List<ReservaIntervalo> buscarIntervalosFinalizados(Reserva reserva) {
        return reservaIntervaloDataProvider.buscarPorReservaESituacao(reserva.getId(), Reserva.Situacao.FINALIZADO);
    }

    private boolean verificarPossuiNumerosUtilizados(List<ReservaIntervalo> intervalos) {
        List<Long> intervalosId = intervalos
            .stream()
            .map(ReservaIntervalo::getId)
            .collect(Collectors.toList());
        return reservaIntervaloNumeroDataProvider
            .existeUtilizadoPorIntervalo(intervalosId);
    }
}
