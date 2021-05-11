package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.RemoverReservaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.RemoverReservaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.RemoverReservaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.exception.ReservaComNumeroUtilizadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoverReservaUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    private RemoverReservaUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new RemoverReservaUseCaseImpl(
            reservaDataProvider,
            patrimonioDataProvider,
            reservaIntervaloDataProvider,
            reservaIntervaloNumeroDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        useCase.executar(new RemoverReservaInputData());
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarReserva() {
        final Long id = 1L;

        when(reservaDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(new RemoverReservaInputData(id));
    }

    @Test
    public void deveRemoverReservaSemIntervalos() {
        final Long id = 1L;
        final Reserva reserva = Reserva.builder()
            .id(id)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong())).thenReturn(Collections.emptyList());

        useCase.executar(new RemoverReservaInputData(id));

        verify(reservaDataProvider, times(1)).remover(reserva.getId());
        verify(reservaIntervaloDataProvider).buscarPorReserva(reserva.getId());
        verifyNoMoreInteractions(reservaIntervaloDataProvider);
        verifyZeroInteractions(reservaIntervaloNumeroDataProvider);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test
    public void deveRemoverReservaComIntervalosSemNumeros() {
        final Long id = 1L;
        final Reserva reserva = Reserva.builder()
            .id(id)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .build();

        final ReservaIntervalo reservaIntervalo = ReservaIntervalo.builder()
            .id(1L)
            .reserva(reserva)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong()))
            .thenReturn(Collections.singletonList(reservaIntervalo));

        when(reservaIntervaloNumeroDataProvider.existePorIntervalo(anyList())).thenReturn(false);

        useCase.executar(new RemoverReservaInputData(id));

        verify(reservaIntervaloDataProvider, times(1))
            .remover(Collections.singletonList(reservaIntervalo.getId()));

        verify(reservaIntervaloNumeroDataProvider).existePorIntervalo(List.of(1L));
        verifyNoMoreInteractions(reservaIntervaloNumeroDataProvider);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test
    public void deveRemoverReservaSemNumeroUtilizado() {
        final Long id = 1L;
        final Reserva reserva = Reserva.builder()
            .id(id)
            .situacao(Reserva.Situacao.FINALIZADO)
            .build();

        final ReservaIntervalo reservaIntervalo = ReservaIntervalo.builder()
            .id(1L)
            .reserva(reserva)
            .situacao(ReservaIntervalo.Situacao.FINALIZADO)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong()))
            .thenReturn(Collections.singletonList(reservaIntervalo));

        when(reservaIntervaloNumeroDataProvider.existePorIntervalo(anyList())).thenReturn(true);

        when(reservaIntervaloNumeroDataProvider.existeNumeroUtilizadoPorIntervalo(anyList())).thenReturn(false);

        useCase.executar(new RemoverReservaInputData(id));

        verify(reservaIntervaloNumeroDataProvider, times(1))
            .removerPorIntervaloId(List.of(1L));
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test(expected = ReservaComNumeroUtilizadoException.class)
    public void deveFalharAoRemoverReservaComNumerosUtilizados() {
        final Long id = 1L;
        final Reserva reserva = Reserva.builder()
            .id(id)
            .situacao(Reserva.Situacao.FINALIZADO)
            .build();

        final ReservaIntervalo reservaIntervalo = ReservaIntervalo.builder()
            .id(1L)
            .reserva(reserva)
            .situacao(ReservaIntervalo.Situacao.FINALIZADO)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong()))
            .thenReturn(Collections.singletonList(reservaIntervalo));

        when(reservaIntervaloNumeroDataProvider.existePorIntervalo(anyList())).thenReturn(true);

        when(reservaIntervaloNumeroDataProvider.existeNumeroUtilizadoPorIntervalo(anyList())).thenReturn(true);

        useCase.executar(new RemoverReservaInputData(id));

        verifyNoMoreInteractions(reservaDataProvider);
        verifyNoMoreInteractions(reservaIntervaloDataProvider);
        verifyNoMoreInteractions(reservaIntervaloNumeroDataProvider);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }
}
