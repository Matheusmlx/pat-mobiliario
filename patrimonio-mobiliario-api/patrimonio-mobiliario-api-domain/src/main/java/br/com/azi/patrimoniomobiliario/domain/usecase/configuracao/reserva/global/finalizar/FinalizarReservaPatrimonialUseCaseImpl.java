package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervaloNumero;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception.ReservaFinalizadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception.ReservaNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception.ReservaNaoPossuiIntervalosEmElaboracaoException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class FinalizarReservaPatrimonialUseCaseImpl implements FinalizarReservaPatrimonialUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Override
    public void executar(FinalizarReservaPatrimonialInputData inputData) {
        validarDadosEntrada(inputData);

        reservaDataProvider.bloquearEntidade();

        reservaIntervaloDataProvider.bloquearEntidade();

        final Reserva reserva = buscarReserva(inputData, criarFiltro());
        verificarSeReservaFinalizada(reserva);

        List<ReservaIntervalo> intervalos = buscarIntervalosEmElaboracao(reserva);
        verificarSeReservaPossuiIntervalosEmElaboracao(intervalos);

        finalizarIntervalos(intervalos);

        alterarSituacaoReserva(reserva);
        salvarReserva(reserva);
    }

    private void validarDadosEntrada(FinalizarReservaPatrimonialInputData inputData) {
        Validator.of(inputData)
            .validate(FinalizarReservaPatrimonialInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo")
            .get();
    }

    private Reserva.Filtro criarFiltro() {
        Reserva.Filtro filtro = new Reserva.Filtro();

        final SessaoUsuario sessaoUsuario = sessaoUsuarioDataProvider.get();
        filtro.setUsuarioId(sessaoUsuario.getId());
        filtro.setFuncoes(List.of(
                PermissaoMobiliarioConstants.NORMAL.getDescription(),
                PermissaoMobiliarioConstants.CONSULTA.getDescription()
        ));

        return filtro;
    }

    private Reserva buscarReserva(FinalizarReservaPatrimonialInputData inputData, Reserva.Filtro filtro) {
        return reservaDataProvider.buscarPorId(inputData.getReservaId(), filtro)
            .orElseThrow(ReservaNaoEncontradaException::new);
    }

    private void verificarSeReservaFinalizada(Reserva reserva) {
        if (Reserva.Situacao.FINALIZADO.equals(reserva.getSituacao())) {
            throw new ReservaFinalizadaException();
        }
    }

    private List<ReservaIntervalo> buscarIntervalosEmElaboracao(Reserva reserva) {
        return reservaIntervaloDataProvider.buscarPorReservaESituacao(reserva.getId(), Reserva.Situacao.EM_ELABORACAO);
    }

    private void verificarSeReservaPossuiIntervalosEmElaboracao(List<ReservaIntervalo> intervalos) {
        if (intervalos.isEmpty()) {
            throw new ReservaNaoPossuiIntervalosEmElaboracaoException();
        }
    }

    private void finalizarIntervalos(List<ReservaIntervalo> intervalos) {
        for (ReservaIntervalo intervalo : intervalos) {
            intervalo.setSituacao(ReservaIntervalo.Situacao.FINALIZADO);
            intervalo.setDataFinalizacao(LocalDateTime.now());
            gerarNumerosIntervalo(intervalo);
        }

        reservaIntervaloDataProvider.salvar(intervalos);
    }

    private void gerarNumerosIntervalo(ReservaIntervalo intervalo) {
        List<ReservaIntervaloNumero> numerosIntervalo = new ArrayList<>();

        for (Long numero = intervalo.getNumeroInicio(); numero <= intervalo.getNumeroFim(); numero++) {
            numerosIntervalo.add(
                ReservaIntervaloNumero
                    .builder()
                    .numero(numero)
                    .reservaIntervalo(intervalo)
                    .utilizado(false)
                    .build()
            );
        }

        reservaIntervaloNumeroDataProvider.salvar(numerosIntervalo);
    }

    private void alterarSituacaoReserva(Reserva reserva) {
        if (reserva.getQuantidadeRestante().compareTo(0L) == 0) {
            reserva.setSituacao(Reserva.Situacao.FINALIZADO);
        } else {
            reserva.setSituacao(Reserva.Situacao.PARCIAL);
        }
    }

    private void salvarReserva(Reserva reserva) {
        reservaDataProvider.salvar(reserva);
    }
}
