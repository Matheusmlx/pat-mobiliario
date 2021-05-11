package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.converter.BuscarProximoNumeroReservaPatrimonialOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarProximoNumeroReservaUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @InjectMocks
    private BuscarProximoNumeroReservaPatrimonialOutputDataConverter outputDataConverter;

    private BuscarProximoNumeroReservaPatrimonialUseCase useCase;

    @Before
    public void setUp() {
        useCase = new BuscarProximoNumeroReservaPatrimonialUseCaseImpl(
            reservaDataProvider,
            patrimonioDataProvider,
            outputDataConverter
        );
    }

    @Test
    public void deveRetornarUmQuandoNaoPossuirReservaEPatrimonio() {
        when(reservaDataProvider.buscarReservaComMaiorNumeroFim())
            .thenReturn(Optional.empty());

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero())
            .thenReturn(Optional.empty());

        BuscarProximoNumeroReservaPatrimonialOutputData outputData = useCase.executar();

        Assert.assertEquals(Long.valueOf(1), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarProximoNumeroAposReserva() {
        when(reservaDataProvider.buscarReservaComMaiorNumeroFim())
            .thenReturn(Optional.of(
                Reserva
                    .builder()
                    .numeroInicio(100L)
                    .numeroFim(150L)
                    .build()
            ));

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero())
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .numero("0000000100")
                    .build()
            ));

        BuscarProximoNumeroReservaPatrimonialOutputData outputData = useCase.executar();

        Assert.assertEquals(Long.valueOf(151), outputData.getProximoNumero());
    }

    @Test
    public void deveRetornarProximoNumeroAposPatrimonio() {
        when(reservaDataProvider.buscarReservaComMaiorNumeroFim())
            .thenReturn(Optional.of(
                Reserva
                    .builder()
                    .numeroInicio(100L)
                    .numeroFim(150L)
                    .build()
            ));

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero())
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .numero("0000000200")
                    .build()
            ));

        BuscarProximoNumeroReservaPatrimonialOutputData outputData = useCase.executar();

        Assert.assertEquals(Long.valueOf(201), outputData.getProximoNumero());
    }

}
