package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.converter.BuscarIntervaloDisponivelOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception.QuantidadeInsuficienteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception.QuantidadeInvalidaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarIntervaloDisponivelUseCaseImpl implements BuscarIntervaloDisponivelUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final BuscarIntervaloDisponivelOutputDataConverter outputDataConverter;

    @Override
    public BuscarIntervaloDisponivelOutputData executar(BuscarIntervaloDisponivelInputData inputData) {
        validarDadosEntrada(inputData);
        setarDadosQuantidadeReservada(inputData);
        Reserva reserva = buscarReserva(inputData);
        verificarSeQuantidadeValida(inputData);
        verificarSeReservaPossuiQuantidade(inputData, reserva);

        Long numeroInicio = buscarProximoNumeroDisponivel(inputData, reserva);
        Long numeroFim = (numeroInicio + inputData.getQuantidadeReservada() - 1);

        return outputDataConverter.to(numeroInicio, numeroFim);
    }

    private void validarDadosEntrada(BuscarIntervaloDisponivelInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarIntervaloDisponivelInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo")
            .validate(BuscarIntervaloDisponivelInputData::getQuantidadeReservada, Objects::nonNull, "Quantidade é nula")
            .validate(BuscarIntervaloDisponivelInputData::getOrgaoId, Objects::nonNull, "Id do órgão é nulo")
            .get();
    }

    private void setarDadosQuantidadeReservada(BuscarIntervaloDisponivelInputData inputData) {
        inputData.setItems(inputData.getItems().stream().filter(item -> Objects.nonNull(item.getQuantidadeReservada())).collect(Collectors.toList()));
    }

    private Reserva buscarReserva(BuscarIntervaloDisponivelInputData inputData) {
        return reservaDataProvider.buscarPorId(inputData.getReservaId()).orElseThrow(ReservaNaoEncontradaException::new);
    }

    private void verificarSeQuantidadeValida(BuscarIntervaloDisponivelInputData inputData) {
        if (inputData.getQuantidadeReservada() <= 0) {
            throw new QuantidadeInvalidaException();
        }
    }

    private void verificarSeReservaPossuiQuantidade(BuscarIntervaloDisponivelInputData inputData, Reserva reserva) {
        final Long quantidadeRestante = reserva.getQuantidadeRestante() - inputData.getItems().stream()
            .filter(intervalo -> !intervalo.getOrgaoId().equals(inputData.getOrgaoId()))
            .mapToLong(BuscarIntervaloDisponivelInputData.Item::getQuantidadeReservada)
            .sum();

        if (inputData.getQuantidadeReservada() > quantidadeRestante) {
            throw new QuantidadeInsuficienteException();
        }
    }

    private Long buscarProximoNumeroDisponivel(BuscarIntervaloDisponivelInputData inputData, Reserva reserva) {
        Optional<ReservaIntervalo> reservaIntervalo = reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(reserva.getId());
        Long proximoNumeroLista = obterProximoNumeroLista(inputData, reserva);

        if (reservaIntervalo.isPresent()) {
            Long proximoNumeroIntervalo = reservaIntervalo.get().getNumeroFim() + 1;

            if (proximoNumeroIntervalo.compareTo(proximoNumeroLista) > 0) {
                return proximoNumeroIntervalo;
            }
        }

        return proximoNumeroLista;
    }

    private Long obterProximoNumeroLista(BuscarIntervaloDisponivelInputData inputData, Reserva reserva) {
        if (Objects.isNull(inputData.getItems()) || inputData.getItems().isEmpty()) {
            return reserva.getNumeroInicio();
        }

        Long proximoNumero = reserva.getNumeroInicio();
        for (BuscarIntervaloDisponivelInputData.Item item : inputData.getItems()) {
            if (!inputData.getOrgaoId().equals(item.getOrgaoId()) &&
                item.getNumeroFim() >= proximoNumero) {
                proximoNumero = item.getNumeroFim() + 1;
            }
        }

        return proximoNumero;
    }
}
