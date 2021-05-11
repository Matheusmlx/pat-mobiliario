package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter.BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter.BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimoniosEstornadosPorIncorporacaoUseCaseTest {

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoDadosEntradaIncompleto() {
        BuscarPatrimoniosEstornadosPorIncorporacaoUseCase useCase = new BuscarPatrimoniosEstornadosPorIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            new BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter(),
            new BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter()
        );

        BuscarPatrimoniosEstornadosPorIncorporacaoInputData incorporacaoInputData = BuscarPatrimoniosEstornadosPorIncorporacaoInputData
            .builder()
            .page(1L)
            .sort("numero")
            .direction("ASC")
            .build();

        useCase.executar(incorporacaoInputData);
    }

    @Test
    public void deveBuscarPatrimoniosEstornados() {
        BuscarPatrimoniosEstornadosPorIncorporacaoUseCase useCase = new BuscarPatrimoniosEstornadosPorIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            new BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter(),
            new BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter()
        );

        LocalDateTime dataEstorno = DateUtils.asLocalDateTime(new Date());

        BuscarPatrimoniosEstornadosPorIncorporacaoInputData inputData = BuscarPatrimoniosEstornadosPorIncorporacaoInputData
            .builder()
            .incorporacaoId(12L)
            .page(1L)
            .size(10L)
            .sort("numero")
            .direction("ASC")
            .build();

        Patrimonio.Filtro filtro = Patrimonio.Filtro
            .builder()
            .incorporacaoId(12L)
            .itensIncorporacaoId(List.of(1L, 2L))
            .build();

        filtro.setPage(0L);
        filtro.setSize(10L);
        filtro.setSort("numero");
        filtro.setDirection("ASC");

        List<ItemIncorporacao> itensIncorporacao = List.of(
            ItemIncorporacao
                .builder()
                .id(1L)
                .codigo("0053235")
                .descricao("Caminhão semi-pesado, zero quilômetro, cabina semi-avançada,motor diesel,turbinado, potência mínima de 200 cv, branco equipado com 3ø eixo, com dispositivo de leva")
                .valorTotal(BigDecimal.valueOf(15.15))
                .quantidade(2)
                .tipoBem("VEICULO")
                .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                .build(),
            ItemIncorporacao
                .builder()
                .id(2L)
                .codigo("000dvf331")
                .descricao("Veículo caminhão leve com as seguintes características técnicas mínimas: -zero quilômetro; -combustível a diesel")
                .valorTotal(BigDecimal.valueOf(22.15))
                .quantidade(3)
                .tipoBem("VEICULO")
                .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                .build());

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(itensIncorporacao);

        when(patrimonioDataProvider.buscarPatrimoniosEstornados(any(Patrimonio.Filtro.class))).thenReturn(
            ListaPaginada
                .<Patrimonio>builder()
                .items(List.of(
                    Patrimonio
                        .builder()
                        .id(1L)
                        .numero("0000000001")
                        .valorAquisicao(BigDecimal.valueOf(7.5))
                        .itemIncorporacao(itensIncorporacao.get(0))
                        .motivoEstorno("Motivo estorno 1")
                        .dataEstorno(dataEstorno)
                        .build(),
                    Patrimonio
                        .builder()
                        .id(2L)
                        .numero("0000000002")
                        .valorAquisicao(BigDecimal.valueOf(7.5))
                        .itemIncorporacao(itensIncorporacao.get(0))
                        .motivoEstorno("Motivo estorno 2")
                        .dataEstorno(dataEstorno)
                        .build(),
                    Patrimonio
                        .builder()
                        .id(3L)
                        .numero("0000000003")
                        .valorAquisicao(BigDecimal.valueOf(22.15))
                        .itemIncorporacao(itensIncorporacao.get(1))
                        .motivoEstorno("Motivo estorno 3")
                        .dataEstorno(dataEstorno)
                        .build()))
                .totalPages(1L)
                .totalElements(3L)
                .build()
        );

        BuscarPatrimoniosEstornadosPorIncorporacaoOutputData outputData = useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(itemIncorporacaoDataProvider, patrimonioDataProvider);

        inOrder.verify(itemIncorporacaoDataProvider, times(1)).buscarItensPorIncorporacaoId(12L);

        inOrder.verify(patrimonioDataProvider, times(1)).buscarPatrimoniosEstornados(filtro);

        assertEquals(Long.valueOf(3), outputData.getTotalElements());
        assertEquals(Long.valueOf(1), outputData.getTotalPages());

        assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        assertEquals("0000000001", outputData.getItems().get(0).getNumero());
        assertEquals(BigDecimal.valueOf(7.5), outputData.getItems().get(0).getValor());
        assertEquals(itensIncorporacao.get(0).getDescricao(), outputData.getItems().get(0).getDescricao());
        assertEquals("Motivo estorno 1", outputData.getItems().get(0).getMotivo());
        assertEquals(DateValidate.formatarData(dataEstorno), outputData.getItems().get(0).getData());

        assertEquals(Long.valueOf(2), outputData.getItems().get(1).getId());
        assertEquals("0000000002", outputData.getItems().get(1).getNumero());
        assertEquals(BigDecimal.valueOf(7.5), outputData.getItems().get(1).getValor());
        assertEquals(itensIncorporacao.get(0).getDescricao(), outputData.getItems().get(1).getDescricao());
        assertEquals("Motivo estorno 2", outputData.getItems().get(1).getMotivo());
        assertEquals(DateValidate.formatarData(dataEstorno), outputData.getItems().get(1).getData());

        assertEquals(Long.valueOf(3), outputData.getItems().get(2).getId());
        assertEquals("0000000003", outputData.getItems().get(2).getNumero());
        assertEquals(BigDecimal.valueOf(22.15), outputData.getItems().get(2).getValor());
        assertEquals(itensIncorporacao.get(1).getDescricao(), outputData.getItems().get(2).getDescricao());
        assertEquals("Motivo estorno 3", outputData.getItems().get(2).getMotivo());
        assertEquals(DateValidate.formatarData(dataEstorno), outputData.getItems().get(2).getData());
    }

}
