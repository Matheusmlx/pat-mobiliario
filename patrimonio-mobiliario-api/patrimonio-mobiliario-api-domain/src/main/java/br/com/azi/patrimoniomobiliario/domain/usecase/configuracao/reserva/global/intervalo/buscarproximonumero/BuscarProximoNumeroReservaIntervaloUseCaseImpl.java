package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.converter.BuscarProximoNumeroReservaIntervaloOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.exception.ReservaCompletaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarProximoNumeroReservaIntervaloUseCaseImpl implements BuscarProximoNumeroReservaIntervaloUseCase {

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaDataProvider reservaDataProvider;

    private final BuscarProximoNumeroReservaIntervaloOutputDataConverter outputDataConverter;

    @Override
    public BuscarProximoNumeroReservaIntervaloOutputData executar(BuscarProximoNumeroReservaIntervaloInputData inputData) {
        validarDadosEntrada(inputData);
        Reserva reserva = buscarReserva(inputData.getReservaId());
        validarReservaPossuiNumerosNaoUtilizados(reserva);
        Long proximoNumeroIntervalo = obterProximoNumeroIntervalo(inputData, reserva);

        return outputDataConverter.to(proximoNumeroIntervalo);
    }

    private void validarDadosEntrada(BuscarProximoNumeroReservaIntervaloInputData inputData) {
        Validator.of(inputData)
                .validate(BuscarProximoNumeroReservaIntervaloInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo")
                .get();
    }

    private Long obterProximoNumeroIntervalo(BuscarProximoNumeroReservaIntervaloInputData inputData, Reserva reserva) {
        Optional<ReservaIntervalo> reservaIntervalo = reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(inputData.getReservaId());

        if (reservaIntervalo.isPresent()) {
            if (ultimoIntervaloDaReserva(reserva, reservaIntervalo.get().getNumeroFim())) {
                return null;
            }
            if (Objects.isNull(inputData.getMaiorNumeroFimIntervalo()) ||
                    inputData.getMaiorNumeroFimIntervalo().compareTo(reservaIntervalo.get().getNumeroFim()) < 0) {
                return reservaIntervalo.get().getNumeroFim() + 1;
            }
            if (ultimoIntervaloDaReserva(reserva, inputData.getMaiorNumeroFimIntervalo())) {
                return null;
            }
            return inputData.getMaiorNumeroFimIntervalo() + 1;
        }
        if (Objects.nonNull(inputData.getMaiorNumeroFimIntervalo()) &&
                inputData.getMaiorNumeroFimIntervalo().compareTo(reserva.getNumeroInicio()) > 0) {
            return inputData.getMaiorNumeroFimIntervalo() + 1;
        }
        return reserva.getNumeroInicio();
    }

    private Reserva buscarReserva(Long reservaId) {
        return reservaDataProvider.buscarPorId(reservaId).orElseThrow(ReservaNaoEncontradaException::new);
    }

    private void validarReservaPossuiNumerosNaoUtilizados(Reserva reserva) {
        if (reserva.getQuantidadeRestante().compareTo(0L) == 0) {
            throw new ReservaCompletaException();
        }
    }

    private Boolean ultimoIntervaloDaReserva(Reserva reserva, Long ultimoNumeroIntervalo) {
        return ultimoNumeroIntervalo.compareTo(reserva.getNumeroFim()) == 0;
    }

}
