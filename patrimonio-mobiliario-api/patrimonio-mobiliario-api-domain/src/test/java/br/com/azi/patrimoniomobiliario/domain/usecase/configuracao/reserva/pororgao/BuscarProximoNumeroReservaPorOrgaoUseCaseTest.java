package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.converter.BuscarProximoNumeroReservaPorOrgaoOutputDataConverter;
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
public class BuscarProximoNumeroReservaPorOrgaoUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @InjectMocks
    private BuscarProximoNumeroReservaPorOrgaoOutputDataConverter outputDataConverter;

    private BuscarProximoNumeroReservaPorOrgaoUseCase useCase;

    @Before
    public void setUp() {
        useCase = new BuscarProximoNumeroReservaPorOrgaoUseCaseImpl(
            reservaDataProvider,
            patrimonioDataProvider,
            outputDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSeOrgaoNaoInformado() {
        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();

        BuscarProximoNumeroReservaPorOrgaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarUmQuandoNaoPossuirReservaEPatrimonio() {
        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(any(Long.class)))
            .thenReturn(Optional.empty());

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(any(Long.class)))
            .thenReturn(Optional.empty());

        BuscarProximoNumeroReservaPorOrgaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarProximoNumeroAposReserva() {
        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(any(Long.class)))
            .thenReturn(Optional.of(
                Reserva
                    .builder()
                    .numeroInicio(100L)
                    .numeroFim(150L)
                    .build()
            ));

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .numero("0000000100")
                    .build()
            ));

        BuscarProximoNumeroReservaPorOrgaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(151), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarProximoNumeroAposPatrimonio() {
        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(any(Long.class)))
            .thenReturn(Optional.of(
                Reserva
                    .builder()
                    .numeroInicio(100L)
                    .numeroFim(150L)
                    .build()
            ));

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .numero("0000000200")
                    .orgao(UnidadeOrganizacional.builder().id(1L).build())
                    .build()
            ));

        BuscarProximoNumeroReservaPorOrgaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(201), outputData.getProximoNumero());
    }

    @Test
    public void naoDeveRetornarProximoNumeroAposPatrimonioSeOrgaoDoPatrimonioDiferente() {
        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(any(Long.class)))
                .thenReturn(Optional.of(
                        Reserva
                                .builder()
                                .numeroInicio(100L)
                                .numeroFim(150L)
                                .build()
                ));

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(any(Long.class)))
                .thenReturn(Optional.empty());

        BuscarProximoNumeroReservaPorOrgaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(151), outputData.getProximoNumero());
    }

}
