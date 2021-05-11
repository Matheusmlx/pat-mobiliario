package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NaturezaDespesaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.converter.EditaItemIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ItemComMesmaNaturezaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ItemNaoExisteException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EditaItemIncorporacaoUseCaseTest {

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Mock
    private ContaContabilDataProvider contaContabilDataProvider;

    @Mock
    private NaturezaDespesaDataProvider naturezaDespesaDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Captor
    private ArgumentCaptor<ItemIncorporacao> argumentCaptorItemIncorporacao;

    private EditaItemIncorporacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new EditaItemIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            incorporacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            naturezaDespesaDataProvider,
            sistemaDeArquivosIntegration,
            new EditaItemIncorporacaoOutputDataConverter());
    }

    @Test
    public void deveEditarItemIncorporacaoQuandoIncorporacaoEstiverEmElaboracao() {
        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .marca("Fiat")
            .modelo("Uno")
            .naturezaDespesa(2L)
            .codigo("1234")
            .descricao("desc")
            .categoria("NENHUM")
            .combustivel("GASOLINA")
            .numeracaoPatrimonial("AUTOMATICA")
            .fabricante("DCS")
            .idIncorporacao(1L)
            .anoFabricacaoModelo("2010/2011")
            .estadoConservacao(2L)
            .quantidade(1000)
            .valorTotal(BigDecimal.valueOf(200))
            .situacao("EM_ELABORACAO")
            .build();

        Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .build();

        Mockito.when(itemIncorporacaoDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(1L)
                .marca("Fiat")
                .modelo("Uno")
                .uriImagem("repo1:patrimoniomobiliario/teste.pdf")
                .naturezaDespesa(NaturezaDespesa.builder().id(1L).build())
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
                .incorporacao(incorporacao)
                .build()));

        Mockito.when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));

        Mockito.when(naturezaDespesaDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(NaturezaDespesa.builder().id(1L).contaContabil(ContaContabil.builder().id(1L).build()).descricao("abc").build()));

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class))).thenReturn(Optional.of(ConfigContaContabil.builder().id(1L).contaContabil(ContaContabil.builder().id(1L).build()).tipoBem(ConfigContaContabil.TipoBem.ARMAMENTO).build()));

        Mockito.when(itemIncorporacaoDataProvider.salvar(any(ItemIncorporacao.class)))
            .thenReturn(ItemIncorporacao
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
                .build());

        EditaItemIncorporacaoOutputData outputData = useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(itemIncorporacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            naturezaDespesaDataProvider,
            sistemaDeArquivosIntegration);

        inOrder.verify(sistemaDeArquivosIntegration, times(1)).removeDefinitiveFile(Arquivo.builder().uri("repo1:patrimoniomobiliario/teste.pdf").build());

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("Fiat", outputData.getMarca());
        Assert.assertEquals("Uno", outputData.getModelo());
        Assert.assertEquals("2010/2011", outputData.getAnoFabricacaoModelo());
        Assert.assertEquals("NENHUM", outputData.getCategoria());
        Assert.assertEquals("1234", outputData.getCodigo());
        Assert.assertEquals("GASOLINA", outputData.getCombustivel());
        Assert.assertEquals("desc", outputData.getDescricao());
        Assert.assertEquals("DCS", outputData.getFabricante());
        Assert.assertEquals("AUTOMATICA", outputData.getNumeracaoPatrimonial());
        Assert.assertEquals("ARMAMENTO", outputData.getTipoBem());
        Assert.assertEquals("repo1:patrimoniomobiliario/teste.pdf", outputData.getUriImagem());
        Assert.assertEquals(Long.valueOf(1), outputData.getContaContabil().getId());
        Assert.assertEquals("123", outputData.getContaContabil().getCodigo());
        Assert.assertEquals("abc", outputData.getContaContabil().getDescricao());
        Assert.assertEquals(Long.valueOf(1), outputData.getEstadoConservacao());
        Assert.assertEquals(Long.valueOf(1), outputData.getNaturezaDespesa().getId());
        Assert.assertEquals("despesa", outputData.getNaturezaDespesa().getDespesa());
        Assert.assertEquals("desc", outputData.getNaturezaDespesa().getDescricao());
        Assert.assertEquals(EditaItemIncorporacaoOutputData.NaturezaDespesa.Situacao.ATIVO, outputData.getNaturezaDespesa().getSituacao());
        Assert.assertEquals(Integer.valueOf(1000), outputData.getQuantidade());
        Assert.assertEquals(BigDecimal.valueOf(200), outputData.getValorTotal());
    }

    @Test
    public void devePermitirEditarQuandoIncorporacaoEstiverComErroProcessamento() {
        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .marca("Fiat")
            .build();

        Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
            .build();

        ItemIncorporacao itemIncorporacaoAtualizado = ItemIncorporacao.builder()
            .id(1L)
            .incorporacao(incorporacao)
            .marca("Fiat")
            .build();

        Mockito.when(itemIncorporacaoDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                ItemIncorporacao.builder()
                    .id(1L)
                    .incorporacao(incorporacao)
                    .build()
                )
            );

        Mockito.when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));

        Mockito.when(itemIncorporacaoDataProvider.salvar(any(ItemIncorporacao.class)))
            .thenReturn(itemIncorporacaoAtualizado);

        useCase.executar(inputData);

        verify(itemIncorporacaoDataProvider, times(1)).salvar(itemIncorporacaoAtualizado);
    }

    @Test
    public void deveEditarItemIncorporacaoSelAlgumasInformacoesNoInput() {
        Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .build();

        ItemIncorporacao itemIncorporacao = ItemIncorporacao
            .builder()
            .id(1L)
            .naturezaDespesa(NaturezaDespesa.builder().id(2L).build())
            .codigo("1234")
            .descricao("desc")
            .categoria(ItemIncorporacao.Categoria.NENHUM)
            .combustivel(ItemIncorporacao.Combustivel.GASOLINA)
            .numeracaoPatrimonial(ItemIncorporacao.NumeracaoPatrimoninal.AUTOMATICA)
            .idIncorporacao(1L)
            .estadoConservacao(EstadoConservacao.builder().id(2L).build())
            .quantidade(1000)
            .valorTotal(BigDecimal.valueOf(200))
            .tipoBem("ARMAMENTO")
            .contaContabil(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build())
            .incorporacao(incorporacao)
            .build();

        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .naturezaDespesa(2L)
            .codigo("1234")
            .descricao("desc")
            .categoria("NENHUM")
            .combustivel("GASOLINA")
            .numeracaoPatrimonial("AUTOMATICA")
            .idIncorporacao(1L)
            .estadoConservacao(2L)
            .quantidade(1000)
            .valorTotal(BigDecimal.valueOf(200))
            .build();

        Mockito.when(itemIncorporacaoDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(1L)
                .marca("Fiat")
                .modelo("Uno")
                .uriImagem("repo1:patrimoniomobiliario/teste.pdf")
                .naturezaDespesa(NaturezaDespesa.builder().id(1L).build())
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
                .incorporacao(incorporacao)
                .build()));

        Mockito.when(naturezaDespesaDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(NaturezaDespesa.builder().id(1L).contaContabil(ContaContabil.builder().id(1L).build()).descricao("abc").build()));

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class))).thenReturn(Optional.of(ConfigContaContabil.builder().id(1L).contaContabil(ContaContabil.builder().id(1L).build()).tipoBem(ConfigContaContabil.TipoBem.ARMAMENTO).build()));

        Mockito.when(itemIncorporacaoDataProvider.salvar(any(ItemIncorporacao.class)))
            .thenReturn(ItemIncorporacao
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
                .build());

        Mockito.when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));

        EditaItemIncorporacaoOutputData outputData = useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(itemIncorporacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            naturezaDespesaDataProvider,
            sistemaDeArquivosIntegration);

        inOrder.verify(sistemaDeArquivosIntegration, times(1)).removeDefinitiveFile(Arquivo.builder().uri("repo1:patrimoniomobiliario/teste.pdf").build());

        inOrder.verify(itemIncorporacaoDataProvider, times(1)).salvar(argumentCaptorItemIncorporacao.capture());
        Assert.assertEquals(itemIncorporacao, argumentCaptorItemIncorporacao.getValue());

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("Fiat", outputData.getMarca());
        Assert.assertEquals("Uno", outputData.getModelo());
        Assert.assertEquals("2010/2011", outputData.getAnoFabricacaoModelo());
        Assert.assertEquals("NENHUM", outputData.getCategoria());
        Assert.assertEquals("1234", outputData.getCodigo());
        Assert.assertEquals("GASOLINA", outputData.getCombustivel());
        Assert.assertEquals("desc", outputData.getDescricao());
        Assert.assertEquals("DCS", outputData.getFabricante());
        Assert.assertEquals("AUTOMATICA", outputData.getNumeracaoPatrimonial());
        Assert.assertEquals("ARMAMENTO", outputData.getTipoBem());
        Assert.assertEquals("repo1:patrimoniomobiliario/teste.pdf", outputData.getUriImagem());
        Assert.assertEquals(Long.valueOf(1), outputData.getContaContabil().getId());
        Assert.assertEquals("123", outputData.getContaContabil().getCodigo());
        Assert.assertEquals("abc", outputData.getContaContabil().getDescricao());
        Assert.assertEquals(Long.valueOf(1), outputData.getEstadoConservacao());
        Assert.assertEquals(Long.valueOf(1), outputData.getNaturezaDespesa().getId());
        Assert.assertEquals("despesa", outputData.getNaturezaDespesa().getDespesa());
        Assert.assertEquals("desc", outputData.getNaturezaDespesa().getDescricao());
        Assert.assertEquals(EditaItemIncorporacaoOutputData.NaturezaDespesa.Situacao.ATIVO, outputData.getNaturezaDespesa().getSituacao());
        Assert.assertEquals(Integer.valueOf(1000), outputData.getQuantidade());
        Assert.assertEquals(BigDecimal.valueOf(200), outputData.getValorTotal());
    }

    @Test
    public void deveEditarItemIncorporacaoComUriDiferente() {
        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .marca("Fiat")
            .modelo("Uno")
            .uriImagem("repo1:patrimoniomobiliario1/teste.pdf")
            .naturezaDespesa(2L)
            .codigo("1234")
            .descricao("desc")
            .categoria("NENHUM")
            .combustivel("GASOLINA")
            .numeracaoPatrimonial("AUTOMATICA")
            .fabricante("DCS")
            .idIncorporacao(1L)
            .anoFabricacaoModelo("2010/2011")
            .estadoConservacao(2L)
            .quantidade(1000)
            .valorTotal(BigDecimal.valueOf(200))
            .build();

        Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .build();

        Mockito.when(itemIncorporacaoDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(1L)
                .marca("Fiat")
                .modelo("Uno")
                .uriImagem("repo1:patrimoniomobiliario/teste.pdf")
                .naturezaDespesa(NaturezaDespesa.builder().id(1L).build())
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
                .incorporacao(incorporacao)
                .build()));

        Mockito.when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));

        Mockito.when(naturezaDespesaDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(NaturezaDespesa.builder().id(1L).contaContabil(ContaContabil.builder().id(1L).build()).descricao("abc").build()));

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class))).thenReturn(Optional.of(ConfigContaContabil.builder().id(1L).contaContabil(ContaContabil.builder().id(1L).build()).tipoBem(ConfigContaContabil.TipoBem.ARMAMENTO).build()));

        Mockito.when(itemIncorporacaoDataProvider.salvar(any(ItemIncorporacao.class)))
            .thenReturn(ItemIncorporacao
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
                .build());

        EditaItemIncorporacaoOutputData outputData = useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(itemIncorporacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            naturezaDespesaDataProvider,
            sistemaDeArquivosIntegration);

        inOrder.verify(sistemaDeArquivosIntegration, times(1)).promote(Arquivo.builder().uri("repo1:patrimoniomobiliario1/teste.pdf").build());

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("Fiat", outputData.getMarca());
        Assert.assertEquals("Uno", outputData.getModelo());
        Assert.assertEquals("2010/2011", outputData.getAnoFabricacaoModelo());
        Assert.assertEquals("NENHUM", outputData.getCategoria());
        Assert.assertEquals("1234", outputData.getCodigo());
        Assert.assertEquals("GASOLINA", outputData.getCombustivel());
        Assert.assertEquals("desc", outputData.getDescricao());
        Assert.assertEquals("DCS", outputData.getFabricante());
        Assert.assertEquals("AUTOMATICA", outputData.getNumeracaoPatrimonial());
        Assert.assertEquals("ARMAMENTO", outputData.getTipoBem());
        Assert.assertEquals("repo1:patrimoniomobiliario/teste.pdf", outputData.getUriImagem());
        Assert.assertEquals(Long.valueOf(1), outputData.getContaContabil().getId());
        Assert.assertEquals("123", outputData.getContaContabil().getCodigo());
        Assert.assertEquals("abc", outputData.getContaContabil().getDescricao());
        Assert.assertEquals(Long.valueOf(1), outputData.getEstadoConservacao());
        Assert.assertEquals(Long.valueOf(1), outputData.getNaturezaDespesa().getId());
        Assert.assertEquals("despesa", outputData.getNaturezaDespesa().getDespesa());
        Assert.assertEquals("desc", outputData.getNaturezaDespesa().getDescricao());
        Assert.assertEquals(EditaItemIncorporacaoOutputData.NaturezaDespesa.Situacao.ATIVO, outputData.getNaturezaDespesa().getSituacao());
        Assert.assertEquals(Integer.valueOf(1000), outputData.getQuantidade());
        Assert.assertEquals(BigDecimal.valueOf(200), outputData.getValorTotal());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalaharQuandoIdForNulo() {
        EditaItemIncorporacaoInputData inputData = new EditaItemIncorporacaoInputData();
        inputData.setMarca("Fiat");

        useCase.executar(inputData);
    }

    @Test(expected = ItemNaoExisteException.class)
    public void deveFalaharQuandoItemNaoExistir() {
        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .marca("Fiat")
            .build();

        Mockito.when(itemIncorporacaoDataProvider.existe(any(Long.class)))
            .thenReturn(false);

        useCase.executar(inputData);

    }

    @Test(expected = ItemComMesmaNaturezaException.class)
    public void deveFalharAoEditarItemIncorporacaoComMesmaNatureza() {
        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .marca("Fiat")
            .modelo("Uno")
            .naturezaDespesa(1L)
            .uriImagem("repo1:patrimoniomobiliario/teste.pdf")
            .build();

        Mockito.when(itemIncorporacaoDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(itemIncorporacaoDataProvider.existeItemComMesmaNatureza(any(EditaItemIncorporacaoInputData.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverEmModoElaboracao() {
        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .marca("Fiat")
            .build();

        Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
            .build();

        Mockito.when(itemIncorporacaoDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                ItemIncorporacao.builder()
                    .id(1L)
                    .incorporacao(incorporacao)
                    .build()
                )
            );

        Mockito.when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));

        useCase.executar(inputData);
    }
}

