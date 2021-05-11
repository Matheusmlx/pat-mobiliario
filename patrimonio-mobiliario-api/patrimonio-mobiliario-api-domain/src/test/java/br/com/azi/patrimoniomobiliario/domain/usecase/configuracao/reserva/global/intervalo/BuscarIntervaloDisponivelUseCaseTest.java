package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.converter.BuscarIntervaloDisponivelOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception.QuantidadeInsuficienteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception.QuantidadeInvalidaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarIntervaloDisponivelUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @InjectMocks
    private BuscarIntervaloDisponivelOutputDataConverter outputDataConverter;

    private BuscarIntervaloDisponivelUseCase useCase;

    @Before
    public void setUp() {
        useCase = new BuscarIntervaloDisponivelUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            outputDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoReservaIdNaoInformado() {
        BuscarIntervaloDisponivelInputData inputData = new BuscarIntervaloDisponivelInputData();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoQuantidadeNaoInformada() {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoReservaNaoEncontrada() {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .quantidadeReservada(5L)
            .orgaoId(1L)
            .items(List.of(BuscarIntervaloDisponivelInputData.Item.builder().orgaoId(1L).quantidadeReservada(12L).build()))
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = QuantidadeInvalidaException.class)
    public void deveFalharSeQuantidadeInformadaInvalida() {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .quantidadeReservada(0L)
            .orgaoId(1L)
            .items(List.of(BuscarIntervaloDisponivelInputData.Item.builder().orgaoId(1L).quantidadeReservada(12L).build()))
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .quantidadeReservada(20L)
            .quantidadeRestante(10L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(reserva));

        useCase.executar(inputData);
    }

    @Test(expected = QuantidadeInsuficienteException.class)
    public void deveFalharSeReservaNaoPossuiNumerosSuficiente() {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .quantidadeReservada(30L)
            .orgaoId(1L)
            .items(Collections.emptyList())
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .quantidadeReservada(20L)
            .quantidadeRestante(20L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(reserva));

        useCase.executar(inputData);
    }

    @Test(expected = QuantidadeInsuficienteException.class)
    public void deveFalharSeReservaNaoPossuiNumerosSuficienteEExisteItensPreenchidos() {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .quantidadeReservada(15L)
            .orgaoId(1L)
            .items(List.of(
                BuscarIntervaloDisponivelInputData.Item.builder()
                    .orgaoId(2L)
                    .quantidadeReservada(10L)
                    .numeroFim(19L)
                    .build()
            ))
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .quantidadeReservada(20L)
            .quantidadeRestante(20L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));

        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarIntervaloValidoReserva() {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .quantidadeReservada(10L)
            .orgaoId(1L)
            .items(Collections.emptyList())
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .quantidadeReservada(20L)
            .quantidadeRestante(20L)
            .numeroInicio(1L)
            .numeroFim(20L)
            .build();

        ReservaIntervalo reservaIntervalo = ReservaIntervalo
            .builder()
            .id(1L)
            .reserva(reserva)
            .quantidadeReservada(20L)
            .numeroInicio(1L)
            .numeroFim(10L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(reserva));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
            .thenReturn(Optional.of(reservaIntervalo));

        BuscarIntervaloDisponivelOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(11), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(20), outputData.getNumeroFim());
    }

    @Test
    public void deveRetornarIntervaloCasoSejaMesmoOrgao() {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .quantidadeReservada(20L)
            .orgaoId(1L)
            .items(List.of(
                BuscarIntervaloDisponivelInputData.Item
                    .builder()
                    .orgaoId(1L)
                    .quantidadeReservada(15L)
                    .numeroFim(15L)
                    .build()
            ))
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .quantidadeReservada(20L)
            .quantidadeRestante(20L)
            .numeroInicio(1L)
            .numeroFim(20L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));

        BuscarIntervaloDisponivelOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(20), outputData.getNumeroFim());
    }
}
