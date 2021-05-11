package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.converter.VisualizarItemIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.exception.ItemIncorporacaoNaoEncontradaException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class VisualizarItemIncorporacaoUseCaseTest {

    @InjectMocks
    private VisualizarItemIncorporacaoUseCaseImpl useCase;

    @InjectMocks
    private VisualizarItemIncorporacaoInputData inputData;

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Before
    public void inicializa(){
        useCase = new VisualizarItemIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            new VisualizarItemIncorporacaoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );

        inputData.setId(1L);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId(){
        useCase.executar(new VisualizarItemIncorporacaoInputData());
    }

    @Test
    public void deveRetornarItemIncorporacao(){
        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(1L)
                .marca("Fiat")
                .modelo("Uno")
                .naturezaDespesa(NaturezaDespesa.builder().id(1L).despesa("despesa").situacao(NaturezaDespesa.Situacao.ATIVO).descricao("desc").build())
                .codigo("1234")
                .descricao("desc")
                .categoria(ItemIncorporacao.Categoria.NENHUM)
                .combustivel(ItemIncorporacao.Combustivel.GASOLINA)
                .numeracaoPatrimonial(ItemIncorporacao.NumeracaoPatrimoninal.AUTOMATICA)
                .fabricante("DCS")
                .idIncorporacao(1L)
                .anoFabricacaoModelo("2010/2011")
                .estadoConservacao(EstadoConservacao.builder().id(1L) .nome("Bom").build())
                .quantidade(1000)
                .valorTotal(BigDecimal.valueOf(200))
                .tipoBem("ARMAMENTO")
                .contaContabil(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build())
                .incorporacao(Incorporacao.builder().id(2L).build())
                .build()));

        VisualizarItemIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("Fiat",outputData.getMarca());
        Assert.assertEquals("Uno",outputData.getModelo());
        Assert.assertEquals("2010/2011",outputData.getAnoFabricacaoModelo());
        Assert.assertEquals("NENHUM",outputData.getCategoria());
        Assert.assertEquals("GASOLINA",outputData.getCombustivel());
        Assert.assertEquals("desc",outputData.getDescricao());
        Assert.assertEquals("DCS",outputData.getFabricante());
        Assert.assertEquals("ARMAMENTO",outputData.getTipoBem());
        Assert.assertEquals("123 - abc",outputData.getContaContabilDescricao());
        Assert.assertEquals("Bom",outputData.getEstadoConservacaoNome());
        Assert.assertEquals(Integer.valueOf(1000),outputData.getQuantidade());
        Assert.assertEquals(BigDecimal.valueOf(200),outputData.getValorTotal());
    }

    @Test(expected = ItemIncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarItemIncorporacao(){
        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }
}
