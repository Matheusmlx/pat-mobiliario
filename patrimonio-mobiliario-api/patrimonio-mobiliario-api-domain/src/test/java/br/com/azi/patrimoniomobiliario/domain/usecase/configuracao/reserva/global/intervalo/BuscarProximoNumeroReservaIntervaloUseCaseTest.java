package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.converter.BuscarProximoNumeroReservaIntervaloOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.exception.ReservaCompletaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.exception.ReservaNaoEncontradaException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarProximoNumeroReservaIntervaloUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @InjectMocks
    private BuscarProximoNumeroReservaIntervaloOutputDataConverter outputDataConverter;

    private BuscarProximoNumeroReservaIntervaloUseCase useCase;

    @Before
    public void setUp() {
        useCase = new BuscarProximoNumeroReservaIntervaloUseCaseImpl(
                reservaIntervaloDataProvider,
                reservaDataProvider,
                outputDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSeIdDaReservaNaoInformado() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .build();

        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharSeReservaNaoEncontrada() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarNumeroInicioReservaQuandoNaoPossuirIntervalo() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(101L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.empty());

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(100), outputData.getProximoNumero());
    }

    @Test(expected = ReservaCompletaException.class)
    public void deveFalharSeReservaCompletaSemQuantidadeRestante() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(0L)
                                .build()
                ));

        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarProximoNumeroDoIntervaloNullSeNumeroFimDoIntervaloIgualNumeroFimDaReserva() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(51L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.of(
                        ReservaIntervalo
                                .builder()
                                .id(1L)
                                .numeroInicio(150L)
                                .numeroFim(200L)
                                .build()
                ));

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertNull(outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarProximoNumeroDoIntervaloSeMaiorNumeroFimDoClienteForNulo() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .maiorNumeroFimIntervalo(null)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(51L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.of(
                        ReservaIntervalo
                                .builder()
                                .id(1L)
                                .numeroInicio(150L)
                                .numeroFim(199L)
                                .build()
                ));

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(200), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarProximoNumeroDoIntervaloSeMaiorNumeroFimDoClienteMenorQueNumeroFimIntervalo() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .maiorNumeroFimIntervalo(149L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(51L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.of(
                        ReservaIntervalo
                                .builder()
                                .id(1L)
                                .numeroInicio(150L)
                                .numeroFim(160L)
                                .build()
                ));

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(161), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarMaiorNumeroFimDoIntervaloNaoCadastradoVindoDoCliente() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .maiorNumeroFimIntervalo(160L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(51L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.of(
                        ReservaIntervalo
                                .builder()
                                .id(1L)
                                .numeroInicio(110L)
                                .numeroFim(150L)
                                .build()
                ));

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(161), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarNumeroInicioReserva() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .maiorNumeroFimIntervalo(0L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(51L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.empty());

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(100), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarNullSeMaiorNumeroFimIntervaloIgualNumeroFimDaReserva() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .maiorNumeroFimIntervalo(200L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(51L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.of(
                        ReservaIntervalo
                                .builder()
                                .id(1L)
                                .numeroInicio(110L)
                                .numeroFim(150L)
                                .build()
                ));

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertNull(outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarNullSeMaiorNumeroFimDoIntervaloIgualNumeroFimDaReserva() {
        BuscarProximoNumeroReservaIntervaloInputData inputData = BuscarProximoNumeroReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .maiorNumeroFimIntervalo(0L)
                .build();

        when(reservaDataProvider.buscarPorId(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .id(1L)
                                .numeroInicio(100L)
                                .numeroFim(200L)
                                .quantidadeRestante(51L)
                                .build()
                ));

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroReserva(any(Long.class)))
                .thenReturn(Optional.of(
                        ReservaIntervalo
                                .builder()
                                .id(1L)
                                .numeroInicio(110L)
                                .numeroFim(200L)
                                .build()
                ));

        BuscarProximoNumeroReservaIntervaloOutputData outputData = useCase.executar(inputData);

        Assert.assertNull(outputData.getProximoNumero());
    }

}
