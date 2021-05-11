package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter.BuscarItensIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter.BuscarItensIncorporacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarItensIncorporacaoUseCaseTest {

    ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;
    ConfigContaContabilDataProvider configContaContabilDataProvider;
    SistemaDeArquivosIntegration sistemaDeArquivosIntegration;
    @Before
    public void inicializa(){
        itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);
        configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
    }

    @Test
    public void deveBuscarItensIncorporacao(){
        BuscarItensIncorporacaoUseCaseImpl useCase = new BuscarItensIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            new BuscarItensIncorporacaoOutputDataConverter(),
            new BuscarItensIncorporacaoFiltroConverter(),
            sistemaDeArquivosIntegration);

        BuscarItensIncorporacaoInputData inputData = BuscarItensIncorporacaoInputData
            .builder()
            .page(1L)
            .size(10L)
            .sort("codigo")
            .direction("ASC")
            .build();

        Mockito.when(itemIncorporacaoDataProvider.buscarPorIncorporacaoId(any(ItemIncorporacao.Filtro.class)))
            .thenReturn(ListaPaginada.<ItemIncorporacao>builder()
                .items(List.of(
                    ItemIncorporacao
                        .builder()
                        .id(1L)
                        .codigo("987456")
                        .descricao("descricao")
                        .situacao(ItemIncorporacao.Situacao.EM_ELABORACAO)
                        .valorTotal(BigDecimal.valueOf(10))
                        .quantidade(1)
                        .tipoBem("ARMAMENTO")
                        .contaContabil(ContaContabil.builder().id(2L).build())
                        .build()))
                .totalElements(1L)
                .totalPages(1L)
                .build());

        BuscarItensIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals("987456", outputData.getItems().get(0).getCodigo());
        Assert.assertEquals("descricao", outputData.getItems().get(0).getDescricao());
        Assert.assertEquals("EM_ELABORACAO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals(BigDecimal.valueOf(10), outputData.getItems().get(0).getValorTotal());
        Assert.assertEquals(java.util.Optional.of(1).get(),outputData.getItems().get(0).getQuantidade());
        Assert.assertEquals("ARMAMENTO", outputData.getItems().get(0).getTipoBem());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalElements());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalPages());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoTemFiltro(){
        BuscarItensIncorporacaoInputData buscarItensIncorporacaoInputData = new BuscarItensIncorporacaoInputData();
        buscarItensIncorporacaoInputData.setSort("ASC");
        BuscarItensIncorporacaoUseCaseImpl buscarItensIncorporacaoUseCase = new BuscarItensIncorporacaoUseCaseImpl(itemIncorporacaoDataProvider,
            new BuscarItensIncorporacaoOutputDataConverter(),
            new BuscarItensIncorporacaoFiltroConverter(),
            sistemaDeArquivosIntegration);

        buscarItensIncorporacaoUseCase.executar(buscarItensIncorporacaoInputData);

    }
}
