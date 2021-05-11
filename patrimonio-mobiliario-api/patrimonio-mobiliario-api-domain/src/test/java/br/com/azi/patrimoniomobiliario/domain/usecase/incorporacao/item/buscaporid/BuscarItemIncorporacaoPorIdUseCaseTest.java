package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscaporid;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.converter.BuscarItemIncorporacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.exception.ItemIncorporacaoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.exception.ItemNaoPertenceAIncorporacaoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarItemIncorporacaoPorIdUseCaseTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId(){
        ItemIncorporacaoDataProvider itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);

        BuscarItemIncorporacaoPorIdUseCaseImpl usecase = new BuscarItemIncorporacaoPorIdUseCaseImpl(
            itemIncorporacaoDataProvider,
            new BuscarItemIncorporacaoPorIdOutputDataConverter()
        );

        usecase.executar(new BuscarItemIncorporacaoPorIdInputData());
    }

    @Test
    public void deveRetornarItemIncorporacao(){
        ItemIncorporacaoDataProvider itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);

        BuscarItemIncorporacaoPorIdUseCaseImpl usecase = new BuscarItemIncorporacaoPorIdUseCaseImpl(
            itemIncorporacaoDataProvider,
            new BuscarItemIncorporacaoPorIdOutputDataConverter()
        );

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(1L)
                .marca("Fiat")
                .modelo("Uno")
                .uriImagem("repo1:patrimoniomobiliario/teste.pdf")
                .naturezaDespesa(NaturezaDespesa.builder().id(1L).despesa("despesa").situacao(NaturezaDespesa.Situacao.ATIVO).descricao("desc").build())
                .codigo("1234")
                .descricao("desc")
                .categoria(ItemIncorporacao.Categoria.NENHUM)
                .combustivel(ItemIncorporacao.Combustivel.GASOLINA)
                .numeracaoPatrimonial(ItemIncorporacao.NumeracaoPatrimoninal.AUTOMATICA)
                .fabricante("DCS")
                .idIncorporacao(1L)
                .anoFabricacaoModelo("2010/2011")
                .estadoConservacao(EstadoConservacao.builder().id(1L).build())
                .quantidade(1000)
                .valorTotal(BigDecimal.valueOf(200))
                .tipoBem("ARMAMENTO")
                .contaContabil(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build())
                .incorporacao(Incorporacao.builder().id(2L).build())
                .situacao(ItemIncorporacao.Situacao.EM_ELABORACAO)
                .build()));

        BuscarItemIncorporacaoPorIdInputData inputData = new BuscarItemIncorporacaoPorIdInputData();
        inputData.setIdItem(1L);
        inputData.setIdIncorporacao(2L);

        BuscarItemIncorporacaoPorIdOutputData outputData = usecase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("Fiat",outputData.getMarca());
        Assert.assertEquals("Uno",outputData.getModelo());
        Assert.assertEquals("2010/2011",outputData.getAnoFabricacaoModelo());
        Assert.assertEquals("NENHUM",outputData.getCategoria());
        Assert.assertEquals("1234",outputData.getCodigo());
        Assert.assertEquals("GASOLINA",outputData.getCombustivel());
        Assert.assertEquals("desc",outputData.getDescricao());
        Assert.assertEquals("DCS",outputData.getFabricante());
        Assert.assertEquals("AUTOMATICA",outputData.getNumeracaoPatrimonial());
        Assert.assertEquals("ARMAMENTO",outputData.getTipoBem());
        Assert.assertEquals("repo1:patrimoniomobiliario/teste.pdf",outputData.getUriImagem());
        Assert.assertEquals(Long.valueOf(1),outputData.getContaContabil().getId());
        Assert.assertEquals("123",outputData.getContaContabil().getCodigo());
        Assert.assertEquals("abc",outputData.getContaContabil().getDescricao());
        Assert.assertEquals(Long.valueOf(1),outputData.getEstadoConservacao());
        Assert.assertEquals(Long.valueOf(1),outputData.getNaturezaDespesa().getId());
        Assert.assertEquals("ATIVO", outputData.getNaturezaDespesa().getSituacao().toString());
        Assert.assertEquals("despesa",outputData.getNaturezaDespesa().getDespesa());
        Assert.assertEquals("desc",outputData.getNaturezaDespesa().getDescricao());
        Assert.assertEquals(Integer.valueOf(1000),outputData.getQuantidade());
        Assert.assertEquals(BigDecimal.valueOf(200),outputData.getValorTotal());
        Assert.assertEquals("EM_ELABORACAO",outputData.getSituacao());
    }

    @Test(expected = ItemIncorporacaoNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarItemIncorporacao(){
        ItemIncorporacaoDataProvider itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);

        BuscarItemIncorporacaoPorIdUseCaseImpl usecase = new BuscarItemIncorporacaoPorIdUseCaseImpl(
            itemIncorporacaoDataProvider,
            new BuscarItemIncorporacaoPorIdOutputDataConverter()
        );

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        BuscarItemIncorporacaoPorIdInputData inputData = BuscarItemIncorporacaoPorIdInputData
            .builder()
            .idItem(1L)
            .idIncorporacao(2L)
            .build();

        usecase.executar(inputData);
    }

    @Test(expected = ItemNaoPertenceAIncorporacaoException.class)
    public void deveFalharQuandoItemNaoExistirNaIncorporacao(){
        ItemIncorporacaoDataProvider itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);

        BuscarItemIncorporacaoPorIdUseCaseImpl usecase = new BuscarItemIncorporacaoPorIdUseCaseImpl(
            itemIncorporacaoDataProvider,
            new BuscarItemIncorporacaoPorIdOutputDataConverter()
        );

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(1L)
                .incorporacao(Incorporacao.builder() .id(2L) .build())
                .descricao("cadastro item teste")
                .codigo("003")
                .build()));

        BuscarItemIncorporacaoPorIdInputData inputData = BuscarItemIncorporacaoPorIdInputData
            .builder()
            .idItem(1L)
            .idIncorporacao(3L)
            .build();

        usecase.executar(inputData);


    }
}
