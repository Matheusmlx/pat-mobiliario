package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.converter.VerificarReservaPossuiNumerosUtilizadosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PossuiNumerosUtilizadosUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    @InjectMocks
    private VerificarReservaPossuiNumerosUtilizadosOutputDataConverter converter;

    private VerificarReservaPossuiNumerosUtilizadosUseCase useCase;

    @Before
    public void setUp() {
        useCase = new VerificarReservaPossuiNumerosUtilizadosUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            reservaIntervaloNumeroDataProvider,
            converter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        VerificarReservaPossuiNumerosUtilizadosInputData inputData = new VerificarReservaPossuiNumerosUtilizadosInputData();
        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoReservaNaoExistir() {
        VerificarReservaPossuiNumerosUtilizadosInputData inputData = new VerificarReservaPossuiNumerosUtilizadosInputData(3L);
        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarSeReservaPossuiNumerosUtilizados() {
        VerificarReservaPossuiNumerosUtilizadosInputData inputData = new VerificarReservaPossuiNumerosUtilizadosInputData(3L);

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(
                    Reserva
                        .builder()
                        .id(3L)
                        .build()
                )
            );

        when(reservaIntervaloDataProvider.buscarPorReservaESituacao(anyLong(), any()))
            .thenReturn(
                List.of(
                    ReservaIntervalo
                        .builder()
                        .id(5L)
                        .build(),
                    ReservaIntervalo
                        .builder()
                        .id(3L)
                        .build()
                ));

        VerificarReservaPossuiNumerosUtilizadosOutputData outputData = useCase.executar(inputData);

        assertFalse(outputData.isPossuiNumerosUtilizados());
    }

    @Test
    public void deveRetornarTrueSeReservaPossuiNumerosUtilizados() {
        VerificarReservaPossuiNumerosUtilizadosInputData inputData = new VerificarReservaPossuiNumerosUtilizadosInputData(3L);

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(
                    Reserva
                        .builder()
                        .id(3L)
                        .build()
                )
            );

        when(reservaIntervaloDataProvider.buscarPorReservaESituacao(anyLong(), any()))
            .thenReturn(
                List.of(
                    ReservaIntervalo
                        .builder()
                        .id(5L)
                        .build(),
                    ReservaIntervalo
                        .builder()
                        .id(3L)
                        .build()
                ));

        when(reservaIntervaloNumeroDataProvider.existeUtilizadoPorIntervalo(anyList()))
            .thenReturn(true);

        VerificarReservaPossuiNumerosUtilizadosOutputData outputData = useCase.executar(inputData);

        assertTrue(outputData.isPossuiNumerosUtilizados());
    }

}
