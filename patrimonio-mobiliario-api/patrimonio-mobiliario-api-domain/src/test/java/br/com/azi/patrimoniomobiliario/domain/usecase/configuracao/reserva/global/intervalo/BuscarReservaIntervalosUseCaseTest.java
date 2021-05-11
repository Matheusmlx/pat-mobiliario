package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter.BuscarReservaIntervalosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter.BuscarReservaIntervalosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarReservaIntervalosUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @InjectMocks
    private BuscarReservaIntervalosFiltroConverter filtroConverter;

    @InjectMocks
    private BuscarReservaIntervalosOutputDataConverter outpuDataConverter;

    private BuscarReservaIntervalosUseCase useCase;

    @Before
    public void setUp() {
        useCase = new BuscarReservaIntervalosUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            filtroConverter,
            outpuDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSeDadosIncompletos() {
        BuscarReservaIntervalosInputData inputData = new BuscarReservaIntervalosInputData();

        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharSeReservaNaoEncontrada() {
        BuscarReservaIntervalosInputData inputData = BuscarReservaIntervalosInputData
            .builder()
            .reservaId(2L)
            .page(1L)
            .size(10L)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarIntervalosAdicionados() {
        BuscarReservaIntervalosInputData inputData = BuscarReservaIntervalosInputData
            .builder()
            .reservaId(2L)
            .page(1L)
            .size(10L)
            .build();

        when(reservaDataProvider.existe(anyLong()))
            .thenReturn(true);

        when(reservaIntervaloDataProvider.buscarIntervalosPorReserva(any(ReservaIntervalo.Filtro.class)))
            .thenReturn(
                ListaPaginada
                    .<ReservaIntervalo>builder()
                    .items(
                        List.of(
                            ReservaIntervalo
                                .builder()
                                .id(1L)
                                .codigo("00001")
                                .orgao(
                                    UnidadeOrganizacional
                                        .builder()
                                        .sigla("DPGE")
                                        .nome("Defensoria Pública Geral do Estado")
                                        .build()
                                )
                                .quantidadeReservada(10L)
                                .preenchimento(ReservaIntervalo.Preenchimento.AUTOMATICO)
                                .numeroInicio(1L)
                                .numeroFim(10L)
                                .build()
                        )
                    )
                    .totalPages(1L)
                    .totalElements(1L)
                    .build()
            );

        BuscarReservaIntervalosOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        assertEquals("00001", outputData.getItems().get(0).getCodigo());
        assertEquals("DPGE - Defensoria Pública Geral do Estado", outputData.getItems().get(0).getOrgao());
        assertEquals(Long.valueOf(10), outputData.getItems().get(0).getQuantidadeReservada());
        assertEquals("AUTOMATICO", outputData.getItems().get(0).getPreenchimento());
        assertEquals(Long.valueOf(1), outputData.getItems().get(0).getNumeroInicio());
        assertEquals(Long.valueOf(10), outputData.getItems().get(0).getNumeroFim());
    }

}
