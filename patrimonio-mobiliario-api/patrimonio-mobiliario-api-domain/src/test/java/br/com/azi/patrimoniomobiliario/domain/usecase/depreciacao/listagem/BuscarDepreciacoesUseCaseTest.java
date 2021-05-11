package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.converter.BuscarDepreciacoesOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarDepreciacoesUseCaseTest {

    public DepreciacaoDataProvider depreciacaoDataProvider;

    public BuscarDepreciacoesUseCaseImpl useCase;

    @Before
    public void start() {
        depreciacaoDataProvider = Mockito.mock(DepreciacaoDataProvider.class);
        useCase = new BuscarDepreciacoesUseCaseImpl(depreciacaoDataProvider, new BuscarDepreciacoesOutputDataConverter());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscarSemId() {
        BuscarDepreciacoesInputData inputData = new BuscarDepreciacoesInputData();
        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        BuscarDepreciacoesInputData inputData = new BuscarDepreciacoesInputData();
        inputData.setId(null);
        BuscarDepreciacoesOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertTrue(outputData.getItems().isEmpty());
    }

    @Test
    public void deveBuscarDepreciacoes() {
        Mockito.when(depreciacaoDataProvider.buscarDepreciacoesPorPatrimonioId(any(Long.class)))
            .thenReturn(
                List.of(
                    Depreciacao.builder()
                        .id(1L)
                        .dataInicial(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                        .dataFinal(LocalDateTime.of(2020, 1, 31, 23, 59, 59))
                        .valorAnterior(BigDecimal.valueOf(100))
                        .valorPosterior(BigDecimal.valueOf(90))
                        .valorSubtraido(BigDecimal.TEN)
                        .taxaAplicada(BigDecimal.TEN)
                        .orgao(UnidadeOrganizacional.builder().id(1L).sigla("AGEPAN").nome("agepan").build())
                        .build(),
                    Depreciacao.builder()
                        .id(2L)
                        .dataInicial(LocalDateTime.of(2020, 2, 1, 0, 0, 0))
                        .dataFinal(LocalDateTime.of(2020, 2, 29, 23, 59, 59))
                        .valorAnterior(BigDecimal.valueOf(90))
                        .valorPosterior(BigDecimal.valueOf(80))
                        .valorSubtraido(BigDecimal.TEN)
                        .taxaAplicada(BigDecimal.TEN)
                        .orgao(UnidadeOrganizacional.builder().id(2L).sigla("DPGE").nome("agepan").build())
                        .build()
                ));

        BuscarDepreciacoesInputData inputData = new BuscarDepreciacoesInputData(1L);
        BuscarDepreciacoesOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals(java.util.Optional.of(2L).get(), outputData.getItems().get(1).getId());
        Assert.assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0, 0), outputData.getItems().get(0).getDataInicial());
        Assert.assertEquals(LocalDateTime.of(2020, 1, 31, 23, 59, 59), outputData.getItems().get(0).getDataFinal());
        Assert.assertEquals(BigDecimal.valueOf(100), outputData.getItems().get(0).getValorAnterior());
        Assert.assertEquals(BigDecimal.valueOf(90), outputData.getItems().get(0).getValorPosterior());
        Assert.assertEquals(BigDecimal.TEN, outputData.getItems().get(0).getValorSubtraido());
        Assert.assertEquals(BigDecimal.TEN, outputData.getItems().get(0).getTaxaAplicada());
        Assert.assertEquals(java.util.Optional.of("AGEPAN").get(), outputData.getItems().get(0).getOrgaoSigla());
        Assert.assertEquals(java.util.Optional.of("DPGE").get(), outputData.getItems().get(1).getOrgaoSigla());
    }

    @Test
    public void deveRetornarListaVaziaQuandoNaoHouverDepreciacoes() {
        Mockito.when(depreciacaoDataProvider.buscarDepreciacoesPorPatrimonioId(any(Long.class)))
            .thenReturn(Collections.emptyList());

        BuscarDepreciacoesInputData inputData = new BuscarDepreciacoesInputData(1L);
        BuscarDepreciacoesOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertTrue(outputData.getItems().isEmpty());
    }
}
