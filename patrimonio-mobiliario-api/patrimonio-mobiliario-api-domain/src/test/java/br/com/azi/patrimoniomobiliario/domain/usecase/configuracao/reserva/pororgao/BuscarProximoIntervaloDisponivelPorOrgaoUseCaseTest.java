package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.converter.BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarProximoIntervaloDisponivelPorOrgaoUseCaseTest {
    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @InjectMocks
    private BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter converter;

    private BuscarProximoIntervaloDisponivelPorOrgaoUseCase useCase;

    @Before
    public void setUp(){
        useCase = new BuscarProximoIntervaloDisponivelPorOrgaoUseCaseImpl(reservaDataProvider,patrimonioDataProvider,converter);
    }


    @Test
    public void deveRetornarIntervaloReserva() {
        BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData = BuscarProximoIntervaloDisponivelPorOrgaoInputData
            .builder()
            .orgaoId(1L)
            .quantidadeReservada(10L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .quantidadeReservada(20L)
            .quantidadeRestante(10L)
            .numeroInicio(1L)
            .numeroFim(20L)
            .build();


        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(inputData.getOrgaoId()))
            .thenReturn(
                Optional.of(reserva)
            );

        BuscarProximoIntervaloDisponivelPorOrgaoOutputData outputData = useCase.executar(inputData);
        assertEquals(Long.valueOf(21L), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(30L), outputData.getNumeroFim());
    }

    @Test
    public void deveConsiderarProximoNumeroDoPatrimonioCasoSejaMaiorQueNumeroReserva(){
        BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData = BuscarProximoIntervaloDisponivelPorOrgaoInputData
            .builder()
            .orgaoId(1L)
            .quantidadeReservada(15L)
            .build();

        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(inputData.getOrgaoId()))
            .thenReturn(
                Optional.empty()
            );

        Patrimonio patrimonio = Patrimonio
            .builder()
            .numero("000010")
            .orgao(UnidadeOrganizacional.builder().id(1L).build())
            .build();

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(inputData.getOrgaoId()))
            .thenReturn(
                Optional.of(patrimonio)
            );

        BuscarProximoIntervaloDisponivelPorOrgaoOutputData outputData = useCase.executar(inputData);
        assertEquals(Long.valueOf(11L),outputData.getNumeroInicio());
        assertEquals(Long.valueOf(25L),outputData.getNumeroFim());
    }

    @Test
    public void deveConsiderarProximoNumeroDisponivelDaReservaCasoSejaMaiorQuePatrimonioNumero(){
        BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData = BuscarProximoIntervaloDisponivelPorOrgaoInputData
            .builder()
            .orgaoId(2L)
            .quantidadeReservada(100L)
            .build();

        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(anyLong()))
            .thenReturn(
                Optional.empty()
            );

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(anyLong()))
            .thenReturn(
                Optional.empty()
            );

        BuscarProximoIntervaloDisponivelPorOrgaoOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1),outputData.getNumeroInicio());
        assertEquals(Long.valueOf(100),outputData.getNumeroFim());
    }
}
