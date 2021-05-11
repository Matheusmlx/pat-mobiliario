package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarPorId;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.converter.BuscarPatrimonioPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.exception.ConfiguracaoDepreciacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.exception.PatrimonioNaoEncontradoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimonioPorIdTest {

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Mock
    private ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    @InjectMocks
    private BuscarPatrimonioPorIdOutputDataConverter outputDataConverter;

    private BuscarPatrimonioPorIdUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarPatrimonioPorIdUseCaseImpl(
            patrimonioDataProvider,
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            configContaContabilDataProvider,
            configuracaoDepreciacaoDataProvider,
            sistemaDeArquivosIntegration,
            outputDataConverter
        );
    }

    @Test
    public void deveBuscarPatrimonioPorId() {

        final long patrimonioId = 1L;
        final long itemIncorporacaoId = 1L;
        final long incorporacaoId = 1L;
        final long configContaContabilId = 1L;

        BuscarPatrimonioPorIdInputData inputData = BuscarPatrimonioPorIdInputData
            .builder()
            .id(1L)
            .build();

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(patrimonioId)
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .orgao(UnidadeOrganizacional.builder().sigla("OR").build())
                    .setor(UnidadeOrganizacional.builder().sigla("SE").nome("Setor").build())
                    .renavam("82975758270")
                    .placa("MWO6460")
                    .projeto("Projeto Teste")
                    .valorLiquido(BigDecimal.valueOf(12900))
                    .valorAquisicao(BigDecimal.valueOf(14900))
                    .valorDepreciacaoMensal(BigDecimal.valueOf(35000))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(1L)
                        .build()
                    )
                    .comodante(
                        Comodante
                            .builder()
                            .id(7L)
                            .nome("Luíz")
                            .build()
                    )
                    .build()));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(
                ConfigContaContabil
                    .builder()
                    .id(configContaContabilId)
                    .vidaUtil(10)
                    .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                    .build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(incorporacaoId)
                .codigo("1554asdf")
                .situacao(Incorporacao.Situacao.FINALIZADO)
                .dataFinalizacao(LocalDateTime.now())
                .dataCadastro(LocalDateTime.now())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(50L)
                    .nome("Orgão")
                    .sigla("OR")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setor(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .nome("Setor")
                    .sigla("SE")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .usuario(Usuario.builder().id(5L).build())
                .reconhecimento(Reconhecimento.builder()
                    .id(27L)
                    .nome("Doação")
                    .notaFiscal(true)
                    .execucaoOrcamentaria(true)
                    .empenho(false)
                    .situacao(Reconhecimento.Situacao.ATIVO)
                    .build())
                .naturezaDespesa(NaturezaDespesa.builder().id(7L).situacao(NaturezaDespesa.Situacao.ATIVO).build())
                .origemConvenio(true)
                .origemFundo(true)
                .build()));

        when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(itemIncorporacaoId)
                .marca("Fiat")
                .modelo("Uno")
                .naturezaDespesa(NaturezaDespesa.builder().id(1L).despesa("despesa").situacao(NaturezaDespesa.Situacao.ATIVO).descricao("desc").build())
                .codigo("1234")
                .descricao("desc")
                .categoria(ItemIncorporacao.Categoria.NENHUM)
                .combustivel(ItemIncorporacao.Combustivel.GASOLINA)
                .numeracaoPatrimonial(ItemIncorporacao.NumeracaoPatrimoninal.AUTOMATICA)
                .fabricante("DCS")
                .configuracaoDepreciacao(ConfiguracaoDepreciacao.builder().id(3L).build())
                .idIncorporacao(1L)
                .anoFabricacaoModelo("2010/2011")
                .estadoConservacao(EstadoConservacao.builder().id(1L).build())
                .quantidade(1000)
                .valorTotal(BigDecimal.valueOf(200))
                .tipoBem("ARMAMENTO")
                .contaContabil(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build())
                .incorporacao(Incorporacao.builder().id(2L).build())
                .build()));

        when(configuracaoDepreciacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ConfiguracaoDepreciacao
                .builder()
                .id(2L)
                .vidaUtil(125)
                .metodo(ConfiguracaoDepreciacao.Metodo.QUOTAS_CONSTANTES)
                .build()));

        BuscarPatrimonioPorIdOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("82975758270", outputData.getRenavam());
        assertEquals("ATIVO", outputData.getSituacao());
        assertEquals("MWO6460", outputData.getPlaca());
        assertEquals("Projeto Teste", outputData.getProjeto());
        assertEquals("NENHUM", outputData.getCategoria());
        assertEquals("2010/2011", outputData.getAnoFabricacaoModelo());
        assertEquals("ARMAMENTO", outputData.getTipo());
        assertEquals("123 - abc", outputData.getContaContabilClassificacao());
        assertEquals("Doação", outputData.getReconhecimento());
        assertEquals("OR", outputData.getOrgao());
        assertEquals("SE - Setor", outputData.getSetor());
        assertEquals("Luíz", outputData.getComodante());
        assertEquals("Fiat",outputData.getMarca());
        assertEquals("desc",outputData.getDescricao());
        assertEquals("QUOTAS_CONSTANTES", outputData.getDadosDepreciacao().getMetodo());
        assertEquals(BigDecimal.valueOf(35000), outputData.getDadosDepreciacao().getValorDepreciacaoMensal());
        assertEquals(Integer.valueOf(125),outputData.getDadosDepreciacao().getVidaUtil());
        assertEquals(BigDecimal.valueOf(12900), outputData.getValorLiquido());

        InOrder inOrder = Mockito.inOrder(patrimonioDataProvider, itemIncorporacaoDataProvider,
            incorporacaoDataProvider, configContaContabilDataProvider);

        inOrder.verify(patrimonioDataProvider, Mockito.times(1)).buscarPorId(patrimonioId);
        inOrder.verify(itemIncorporacaoDataProvider, Mockito.times(1)).buscarPorId(itemIncorporacaoId);
        inOrder.verify(incorporacaoDataProvider, Mockito.times(1)).buscarPorId(incorporacaoId);
        inOrder.verify(configContaContabilDataProvider, Mockito.times(1)).buscarAtualPorContaContabil(configContaContabilId);
    }
    @Test(expected = ConfiguracaoDepreciacaoNaoEncontradaException.class)
    public void deveFalharQuandoConfiguracaoNaoEncontrada() {
        final long patrimonioId = 1L;
        final long itemIncorporacaoId = 1L;
        final long incorporacaoId = 1L;
        final long configContaContabilId = 1L;

        BuscarPatrimonioPorIdInputData inputData = BuscarPatrimonioPorIdInputData
            .builder()
            .id(1L)
            .build();

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(patrimonioId)
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .orgao(UnidadeOrganizacional.builder().sigla("OR").build())
                    .setor(UnidadeOrganizacional.builder().nome("Setor").build())
                    .renavam("82975758270")
                    .placa("MWO6460")
                    .projeto("Projeto Teste")
                    .valorLiquido(BigDecimal.valueOf(12900))
                    .valorAquisicao(BigDecimal.valueOf(14900))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(1L)
                        .build()
                    )
                    .comodante(
                        Comodante
                            .builder()
                            .id(7L)
                            .nome("Luíz")
                            .build()
                    )
                    .build()));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(
                ConfigContaContabil
                    .builder()
                    .id(configContaContabilId)
                    .vidaUtil(10)
                    .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                    .build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(incorporacaoId)
                .codigo("1554asdf")
                .situacao(Incorporacao.Situacao.FINALIZADO)
                .dataFinalizacao(LocalDateTime.now())
                .dataCadastro(LocalDateTime.now())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(50L)
                    .nome("Orgão")
                    .sigla("OR")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setor(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .nome("Setor")
                    .sigla("SE")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .usuario(Usuario.builder().id(5L).build())
                .reconhecimento(Reconhecimento.builder()
                    .id(27L)
                    .nome("Doação")
                    .notaFiscal(true)
                    .execucaoOrcamentaria(true)
                    .empenho(false)
                    .situacao(Reconhecimento.Situacao.ATIVO)
                    .build())
                .naturezaDespesa(NaturezaDespesa.builder().id(7L).situacao(NaturezaDespesa.Situacao.ATIVO).build())
                .origemConvenio(true)
                .origemFundo(true)
                .build()));

        when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ItemIncorporacao
                .builder()
                .id(itemIncorporacaoId)
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
                .estadoConservacao(EstadoConservacao.builder().id(1L).build())
                .quantidade(1000)
                .configuracaoDepreciacao(ConfiguracaoDepreciacao.builder().id(2L).build())
                .valorTotal(BigDecimal.valueOf(200))
                .tipoBem("ARMAMENTO")
                .contaContabil(ContaContabil.builder().id(1L).codigo("123").descricao("abc").build())
                .incorporacao(Incorporacao.builder().id(2L).build())
                .build()));

        when(configuracaoDepreciacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.empty()
        );

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdEhNulo() {

        BuscarPatrimonioPorIdInputData inputData = BuscarPatrimonioPorIdInputData
            .builder()
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoPatrimonioNaoEncontrado() {

        BuscarPatrimonioPorIdInputData inputData = BuscarPatrimonioPorIdInputData
            .builder()
            .id(3L)
            .build();

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }
}
