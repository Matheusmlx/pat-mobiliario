package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.RemoverReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.RemoverReservaIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.RemoverReservaIntervaloUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.IntervaloJaEstaFinalizadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.ReservaFinalizadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.ReservaIntervaloNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoverReservaIntervaloUseCaseTest {

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private RemoverReservaIntervaloUseCase useCase;

    @Before
    public void setUp() {
        useCase = new RemoverReservaIntervaloUseCaseImpl(
                reservaIntervaloDataProvider,
                reservaDataProvider,
                sessaoUsuarioDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdReservaIntevaloNulo() {
        RemoverReservaIntervaloInputData inputData = new RemoverReservaIntervaloInputData();

        useCase.executar(inputData);
    }

    @Test(expected = ReservaIntervaloNaoEncontradaException.class)
    public void deveFalharQuandoReservaIntervaloNaoEncontrado() {
        RemoverReservaIntervaloInputData inputData = RemoverReservaIntervaloInputData
                .builder()
                .reservaIntervaloId(1L)
                .build();

        when(reservaIntervaloDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloJaEstaFinalizadoException.class)
    public void deveFalharQuandoIntervaloNaoEstiverEmElaboracao() {
        ReservaIntervalo reservaIntervalo = ReservaIntervalo.builder()
                .id(1L)
                .situacao(ReservaIntervalo.Situacao.FINALIZADO)
                .build();

        when(reservaIntervaloDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reservaIntervalo));

        useCase.executar(new RemoverReservaIntervaloInputData(1L));
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarReserva() {
        ReservaIntervalo reservaIntervalo = ReservaIntervalo.builder()
                .id(1L)
                .reserva(Reserva.builder().id(1L).build())
                .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                .build();

        when(reservaIntervaloDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reservaIntervalo));

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.empty());

        useCase.executar(new RemoverReservaIntervaloInputData(1L));
    }

    @Test(expected = ReservaFinalizadaException.class)
    public void deveFalharQuandoReservaJaEstiverFinalizada() {
        ReservaIntervalo reservaIntervalo = ReservaIntervalo.builder()
                .id(1L)
                .reserva(Reserva.builder().id(1L).build())
                .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                .build();

        Reserva reserva = Reserva.builder()
                .id(1L)
                .situacao(Reserva.Situacao.FINALIZADO)
                .build();

        when(reservaIntervaloDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reservaIntervalo));

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        useCase.executar(new RemoverReservaIntervaloInputData(1L));
    }

    @Test
    public void deveExcluirIntervaloEDevolverQuantidadeParaReserva() {
        ReservaIntervalo reservaIntervalo = ReservaIntervalo.builder()
            .id(1L)
            .reserva(Reserva.builder().id(1L).build())
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .quantidadeReservada(10L)
            .build();

        Reserva reserva = Reserva.builder()
            .id(1L)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .quantidadeReservada(20L)
            .quantidadeRestante(10L)
            .build();

        Reserva reservaAposRemoverIntervalo = Reserva.builder()
            .id(1L)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .quantidadeReservada(20L)
            .quantidadeRestante(20L)
            .build();

        when(reservaIntervaloDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reservaIntervalo));

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class))).thenReturn(Optional.of(reserva));

        useCase.executar(new RemoverReservaIntervaloInputData(1L));

        verify(reservaIntervaloDataProvider, times(1)).remover(1L);
        verify(reservaDataProvider, times(1)).salvar(reservaAposRemoverIntervalo);
    }

}
