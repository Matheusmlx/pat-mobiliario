package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.exception.ReservaComNumeroUtilizadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RemoverReservaUseCaseImpl implements RemoverReservaUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    @Override
    public void executar(RemoverReservaInputData inputData) {
        validarDadosEntrada(inputData);

        patrimonioDataProvider.bloquearEntidade();

        reservaDataProvider.bloquearEntidade();

        final Reserva reserva = buscarReserva(inputData);

        final List<ReservaIntervalo> intervalos = buscarIntervalos(reserva);

        if (!intervalos.isEmpty()) {
            removerNumerosReserva(intervalos);
            removerIntervalos(intervalos);
        }

        removerReserva(reserva);
    }

    private void validarDadosEntrada(RemoverReservaInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverReservaInputData::getId, Objects::nonNull, "O id da reserva não pode ser nulo.")
            .get();
    }

    private Reserva buscarReserva(RemoverReservaInputData inputData) {
        return reservaDataProvider.buscarPorId(inputData.getId())
            .orElseThrow(ReservaNaoEncontradaException::new);
    }

    private List<ReservaIntervalo> buscarIntervalos(Reserva reserva) {
        return reservaIntervaloDataProvider.buscarPorReserva(reserva.getId());
    }

    private void removerNumerosReserva(List<ReservaIntervalo> intervalos) {
        final List<Long> intervalosId = intervalos.stream()
            .map(ReservaIntervalo::getId)
            .collect(Collectors.toList());

        if (reservaIntervaloNumeroDataProvider.existePorIntervalo(intervalosId)) {
           validarReservaSemNumerosUtilizados(intervalosId);
           reservaIntervaloNumeroDataProvider.removerPorIntervaloId(intervalosId);
        }
    }

    private void validarReservaSemNumerosUtilizados(List<Long> intervalosId) {
        if (reservaIntervaloNumeroDataProvider.existeNumeroUtilizadoPorIntervalo(intervalosId)) {
            throw new ReservaComNumeroUtilizadoException();
        }
    }

    private void removerIntervalos(List<ReservaIntervalo> intervalos) {
        reservaIntervaloDataProvider.remover(
            intervalos.stream()
                .map(ReservaIntervalo::getId)
                .collect(Collectors.toList())
        );
    }

    private void removerReserva(Reserva reserva) {
        reservaDataProvider.remover(reserva.getId());
    }
}
