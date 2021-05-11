package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervaloNumero;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.FinalizarReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.FinalizarReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.FinalizarReservaPatrimonialUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception.ReservaFinalizadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception.ReservaNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception.ReservaNaoPossuiIntervalosEmElaboracaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarReservaPatrimonialTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private FinalizarReservaPatrimonialUseCase useCase;

    @Before
    public void inicializarUseCase() {
        useCase = new FinalizarReservaPatrimonialUseCaseImpl(
                reservaDataProvider,
                reservaIntervaloDataProvider,
                reservaIntervaloNumeroDataProvider,
                sessaoUsuarioDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoIdDaReservaForNulo() {
        useCase.executar(new FinalizarReservaPatrimonialInputData());
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoReservaNaoForEncontrada() {
        final Long reservaId = 1L;

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.empty());

        useCase.executar(new FinalizarReservaPatrimonialInputData(reservaId));
    }

    @Test(expected = ReservaFinalizadaException.class)
    public void deveFalharQuandoReservaJaEstiverFinalizada() {
        final Long reservaId = 1L;
        final Reserva reserva = Reserva.builder()
                .id(reservaId)
                .situacao(Reserva.Situacao.FINALIZADO)
                .build();

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        useCase.executar(new FinalizarReservaPatrimonialInputData(reservaId));
    }

    @Test(expected = ReservaNaoPossuiIntervalosEmElaboracaoException.class)
    public void deveFalharQuandoNaoExistirIntervalosEmElaboracao() {
        final Long reservaId = 1L;
        final Reserva reserva = Reserva.builder()
                .id(reservaId)
                .situacao(Reserva.Situacao.EM_ELABORACAO)
                .build();

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReservaESituacao(anyLong(), any(Reserva.Situacao.class)))
                .thenReturn(Collections.emptyList());

        useCase.executar(new FinalizarReservaPatrimonialInputData(reservaId));
    }

    @Test
    public void deveFinalizarTodosIntervalosEmElaboracaoQuandoFinalizarReserva() {
        final Long reservaId = 1L;
        final Reserva reserva = Reserva.builder()
            .id(reservaId)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .quantidadeReservada(20L)
            .quantidadeRestante(10L)
            .build();

        final ReservaIntervalo intervalo1 = ReservaIntervalo.builder()
            .id(1L)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .quantidadeReservada(5L)
            .numeroInicio(1L)
            .numeroFim(5L)
            .build();

        final ReservaIntervalo intervalo2 = ReservaIntervalo.builder()
            .id(2L)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .quantidadeReservada(5L)
            .numeroInicio(6L)
            .numeroFim(10L)
            .build();

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReservaESituacao(anyLong(), any(Reserva.Situacao.class)))
                .thenReturn(List.of(intervalo1, intervalo2));

        useCase.executar(new FinalizarReservaPatrimonialInputData(reservaId));

        verify(reservaIntervaloDataProvider, times(1)).salvar(List.of(intervalo1, intervalo2));
        verify(reservaDataProvider).bloquearEntidade();
        verify(reservaIntervaloDataProvider).bloquearEntidade();
    }

    @Test
    public void deveGerarOsNumerosDosIntervalosEmElaboracaoQuandoFinalizarReserva() {
        final Long reservaId = 1L;
        final Reserva reserva = Reserva.builder()
            .id(reservaId)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .quantidadeReservada(20L)
            .quantidadeRestante(15L)
            .build();

        final ReservaIntervalo intervalo1 = ReservaIntervalo.builder()
            .id(1L)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .quantidadeReservada(5L)
            .numeroInicio(1L)
            .numeroFim(5L)
            .build();

        final List<ReservaIntervaloNumero> numerosIntervalo = List.of(
            ReservaIntervaloNumero.builder()
                .numero(1L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(2L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(3L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(4L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(5L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build()
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReservaESituacao(anyLong(), any(Reserva.Situacao.class)))
                .thenReturn(List.of(intervalo1));

        useCase.executar(new FinalizarReservaPatrimonialInputData(reservaId));

        verify(reservaIntervaloDataProvider, times(1)).salvar(List.of(intervalo1));
        verify(reservaIntervaloNumeroDataProvider, times(1)).salvar(numerosIntervalo);
        verify(reservaDataProvider).bloquearEntidade();
        verify(reservaIntervaloDataProvider).bloquearEntidade();
    }

    @Test
    public void deveAlterarASituacaoParaParcialQuandoFinalizarReservaEHouverNumerosRestantes() {
        final Long reservaId = 1L;
        final Reserva reserva = Reserva.builder()
            .id(reservaId)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .quantidadeReservada(20L)
            .quantidadeRestante(15L)
            .build();

        final Reserva reservaAtualizada = Reserva.builder()
            .id(reservaId)
            .situacao(Reserva.Situacao.PARCIAL)
            .quantidadeReservada(20L)
            .quantidadeRestante(15L)
            .build();

        final ReservaIntervalo intervalo1 = ReservaIntervalo.builder()
            .id(1L)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .quantidadeReservada(5L)
            .numeroInicio(1L)
            .numeroFim(5L)
            .build();

        final List<ReservaIntervaloNumero> numerosIntervalo = List.of(
            ReservaIntervaloNumero.builder()
                .numero(1L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(2L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(3L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(4L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(5L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build()
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReservaESituacao(anyLong(), any(Reserva.Situacao.class)))
                .thenReturn(List.of(intervalo1));

        useCase.executar(new FinalizarReservaPatrimonialInputData(reservaId));

        verify(reservaIntervaloDataProvider, times(1)).salvar(List.of(intervalo1));
        verify(reservaIntervaloNumeroDataProvider, times(1)).salvar(numerosIntervalo);
        verify(reservaDataProvider, times(1)).salvar(reservaAtualizada);
        verify(reservaDataProvider).bloquearEntidade();
        verify(reservaIntervaloDataProvider).bloquearEntidade();
    }

    @Test
    public void deveAlterarASituacaoParaFinalizadoQuandoFinalizarReservaEUtilizarTodosOsNumerosDisponiveis() {
        final Long reservaId = 1L;
        final Reserva reserva = Reserva.builder()
            .id(reservaId)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .quantidadeReservada(5L)
            .quantidadeRestante(0L)
            .build();

        final Reserva reservaAtualizada = Reserva.builder()
            .id(reservaId)
            .situacao(Reserva.Situacao.FINALIZADO)
            .quantidadeReservada(5L)
            .quantidadeRestante(0L)
            .build();

        final ReservaIntervalo intervalo1 = ReservaIntervalo.builder()
            .id(1L)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .quantidadeReservada(5L)
            .numeroInicio(1L)
            .numeroFim(5L)
            .build();

        final List<ReservaIntervaloNumero> numerosIntervalo = List.of(
            ReservaIntervaloNumero.builder()
                .numero(1L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(2L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(3L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(4L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build(),
            ReservaIntervaloNumero.builder()
                .numero(5L)
                .reservaIntervalo(intervalo1)
                .utilizado(false)
                .build()
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReservaESituacao(anyLong(), any(Reserva.Situacao.class)))
                .thenReturn(List.of(intervalo1));

        useCase.executar(new FinalizarReservaPatrimonialInputData(reservaId));

        verify(reservaIntervaloDataProvider, times(1)).salvar(List.of(intervalo1));
        verify(reservaIntervaloNumeroDataProvider, times(1)).salvar(numerosIntervalo);
        verify(reservaDataProvider, times(1)).salvar(reservaAtualizada);
        verify(reservaDataProvider).bloquearEntidade();
        verify(reservaIntervaloDataProvider).bloquearEntidade();
    }
}
