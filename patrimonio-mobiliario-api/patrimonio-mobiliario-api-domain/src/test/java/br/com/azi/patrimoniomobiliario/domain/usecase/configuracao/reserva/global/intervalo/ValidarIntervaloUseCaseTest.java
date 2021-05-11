package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.IntervaloForaDaReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.NumeroInicioMaiorQueNumeroFimException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidarIntervaloUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;


    private ValidarIntervaloUseCase useCase;

    @Before
    public void setUp() {
        useCase = new ValidarIntervaloUseCaseImpl(
                reservaDataProvider,
                reservaIntervaloDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoReservaIdNaoInformado() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setNumeroInicio(100L);
        inputData.setNumeroFim(200L);

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNumeoInicioNaoInformado() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroFim(200L);

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNumeoFimNaoInformado() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(100L);

        useCase.executar(inputData);
    }

    @Test(expected = NumeroInicioMaiorQueNumeroFimException.class)
    public void deveFalharQuandoFimMaiorQueInicio() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(101L);
        inputData.setNumeroFim(100L);


        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoReservaNaoEncontrada() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(100L);
        inputData.setNumeroFim(200L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloForaDaReservaException.class)
    public void deveFalharQuandoNumeroInicioIntervaloMenorQueNumeroInicioReserva() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(99L);
        inputData.setNumeroFim(150L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .build()
                ));


        useCase.executar(inputData);
    }

    @Test(expected = IntervaloForaDaReservaException.class)
    public void deveFalharQuandoNumeroFimIntervaloMaiorQueNumeroFimReserva() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(101L);
        inputData.setNumeroFim(201L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .build()
                ));


        useCase.executar(inputData);
    }

    @Test(expected = IntervaloForaDaReservaException.class)
    public void deveFalharQuandoNumeroInicioIntervaloMaiorQueNumeroFimReserva() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(201L);
        inputData.setNumeroFim(300L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .build()
                ));


        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloJaExiste() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(100L);
        inputData.setNumeroFim(150L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.existeIntervaloNaReserva(any(Long.class),any(Long.class), any(Long.class)))
                .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveValidarTamanhoIntervaloIgualReserva() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(100L);
        inputData.setNumeroFim(200L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.existeIntervaloNaReserva(any(Long.class),any(Long.class), any(Long.class)))
                .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test
    public void deveValidarIntervaloDeTamanhoUmNoInicio() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(100L);
        inputData.setNumeroFim(100L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.existeIntervaloNaReserva(any(Long.class),any(Long.class), any(Long.class)))
                .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test
    public void deveValidarIntervaloDeTamanhoUmNoFim() {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(200L);
        inputData.setNumeroFim(200L);

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.existeIntervaloNaReserva(any(Long.class),any(Long.class), any(Long.class)))
                .thenReturn(false);

        useCase.executar(inputData);
    }

}
