package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.IntervaloForaDaReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.NumeroInicioMaiorQueNumeroFimException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class ValidarIntervaloUseCaseImpl implements ValidarIntervaloUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Override
    public void executar(ValidarIntervaloInputData inputData) {
        validarDadosEntrada(inputData);

        validarNumeroFimMaiorNumeroInicio(inputData);

        Reserva reserva = buscarReserva(inputData);

        validarIntervaloDentroReserva(reserva, inputData);

        validarIntervaloUnico(reserva, inputData);
    }


    private void validarDadosEntrada(ValidarIntervaloInputData inputData) {
        Validator.of(inputData)
                .validate(ValidarIntervaloInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo")
                .validate(ValidarIntervaloInputData::getNumeroInicio, Objects::nonNull, "Número de início é nulo")
                .validate(ValidarIntervaloInputData::getNumeroFim, Objects::nonNull, "Número de fim é nulo")
                .get();
    }

    private void validarNumeroFimMaiorNumeroInicio(ValidarIntervaloInputData inputData) {
        if (inputData.getNumeroInicio().compareTo(inputData.getNumeroFim()) > 0) {
            throw new NumeroInicioMaiorQueNumeroFimException();
        }
    }

    private Reserva buscarReserva(ValidarIntervaloInputData inputData) {
        return reservaDataProvider.buscarPorId(inputData.getReservaId()).orElseThrow(ReservaNaoEncontradaException::new);
    }

    private void validarIntervaloDentroReserva(Reserva reserva, ValidarIntervaloInputData inputData) {
        if (inputData.getNumeroInicio().compareTo(reserva.getNumeroInicio()) < 0 ||
                inputData.getNumeroInicio().compareTo(reserva.getNumeroFim()) > 0 ||
                inputData.getNumeroFim().compareTo(reserva.getNumeroFim()) > 0) {
            throw new IntervaloForaDaReservaException();
        }
    }

    private void validarIntervaloUnico(Reserva reserva, ValidarIntervaloInputData inputData) {
        Boolean existe = reservaIntervaloDataProvider.existeIntervaloNaReserva(inputData.getNumeroInicio(), inputData.getNumeroFim(), reserva.getId());
        if (Boolean.TRUE.equals(existe)) {
            throw new IntervaloEmUsoException();
        }
    }
}
