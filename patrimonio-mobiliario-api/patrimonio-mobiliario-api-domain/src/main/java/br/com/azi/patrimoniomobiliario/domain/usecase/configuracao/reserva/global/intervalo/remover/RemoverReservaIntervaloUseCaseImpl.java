package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.IntervaloJaEstaFinalizadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.ReservaFinalizadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.ReservaIntervaloNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class RemoverReservaIntervaloUseCaseImpl implements RemoverReservaIntervaloUseCase {

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaDataProvider reservaDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Override
    public void executar(RemoverReservaIntervaloInputData inputData) {
        validarDadosEntrada(inputData);

        final ReservaIntervalo reservaIntervalo = buscar(inputData);
        verificarIntervaloEmElaboracao(reservaIntervalo);

        Reserva.Filtro filtro = criarFiltro();
        final Reserva reserva = buscarReserva(reservaIntervalo.getReserva().getId(), filtro);
        verificarSeReservaNaoEstaFinalizada(reserva);
        devolverQuantidadeUtilizadaParaReserva(reserva, reservaIntervalo);
        salvarReserva(reserva);

        removerIntervalo(reservaIntervalo);
    }

    private ReservaIntervalo buscar(RemoverReservaIntervaloInputData inputData) {
        return reservaIntervaloDataProvider.buscarPorId(inputData.getReservaIntervaloId())
            .orElseThrow(ReservaIntervaloNaoEncontradaException::new);
    }

    private void validarDadosEntrada(RemoverReservaIntervaloInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverReservaIntervaloInputData::getReservaIntervaloId, Objects::nonNull, "Id do intervalo da reserva é nulo")
            .get();
    }

    private void verificarIntervaloEmElaboracao(ReservaIntervalo reservaIntervalo) {
        if (!ReservaIntervalo.Situacao.EM_ELABORACAO.equals(reservaIntervalo.getSituacao())) {
            throw new IntervaloJaEstaFinalizadoException();
        }
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

    private Reserva buscarReserva(Long id, Reserva.Filtro filtro) {
        return reservaDataProvider.buscarPorId(id, filtro).orElseThrow(ReservaNaoEncontradaException::new);
    }

    private void verificarSeReservaNaoEstaFinalizada(Reserva reserva) {
        if (Reserva.Situacao.FINALIZADO.equals(reserva.getSituacao())) {
            throw new ReservaFinalizadaException();
        }
    }

    private void devolverQuantidadeUtilizadaParaReserva(Reserva reserva, ReservaIntervalo reservaIntervalo) {
        reserva.setQuantidadeRestante(reserva.getQuantidadeRestante() + reservaIntervalo.getQuantidadeReservada());
    }

    private void salvarReserva(Reserva reserva) {
        reservaDataProvider.salvar(reserva);
    }

    private void removerIntervalo(ReservaIntervalo reservaIntervalo) {
        reservaIntervaloDataProvider.remover(reservaIntervalo.getId());
    }

}
