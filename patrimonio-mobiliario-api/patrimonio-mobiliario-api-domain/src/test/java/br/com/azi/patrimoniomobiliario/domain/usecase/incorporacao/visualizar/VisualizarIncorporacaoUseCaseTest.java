package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.converter.VisualizarIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class VisualizarIncorporacaoUseCaseTest {

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    private VisualizarIncorporacaoUseCase useCase;

    @Before
    public void inicializa() {
        useCase = new VisualizarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            patrimonioDataProvider,
            new VisualizarIncorporacaoOutputDataConverter()
        );
    }

    @Test
    public void deveBuscarIncorporacaoPorId() {
        VisualizarIncorporacaoInputData inputData = VisualizarIncorporacaoInputData
            .builder()
            .id(1L)
            .build();


        LocalDateTime dataHoraRecebimento = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
        LocalDateTime dataNota = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
        LocalDateTime dataNotaLancamentoContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .codigo("1554asdf")
                .dataRecebimento(dataHoraRecebimento)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .numProcesso("31415")
                .justificativa("criando incorporacao")
                .fornecedor(
                    Fornecedor.builder()
                        .id(1L)
                        .cpfCnpj("12345678912345")
                        .nome("TESTE")
                        .situacao(Fornecedor.Situacao.ATIVO)
                        .build()
                )
                .nota("7")
                .valorNota(BigDecimal.valueOf(7))
                .dataNota(dataNota)
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(50L)
                    .sigla("ORGAO")
                    .nome("orgao")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setor(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .sigla("SETOR")
                    .nome("setor")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .convenio(Convenio.builder().id(10L).nome("convenio").build())
                .fundo(UnidadeOrganizacional
                    .builder()
                    .id(3L)
                    .sigla("FUNDO")
                    .nome("Fundo")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .projeto("Projeto Teste")
                .usuario(Usuario.builder().id(5L).build())
                .reconhecimento(Reconhecimento.builder().id(27L).nome("reconhecimento").situacao(Reconhecimento.Situacao.ATIVO).build())
                .empenho(Empenho.builder().id(2L).build())
                .naturezaDespesa(NaturezaDespesa.builder().id(7L).situacao(NaturezaDespesa.Situacao.ATIVO).build())
                .origemConvenio(true)
                .origemFundo(true)
                .origemProjeto(true)
                .origemComodato(true)
                .notaLancamentoContabil(NotaLancamentoContabil
                    .builder()
                    .id(1L)
                    .numero("2020NL123456")
                    .dataLancamento(dataNotaLancamentoContabil)
                    .build())
                .comodante(
                    Comodante
                        .builder()
                        .id(1L)
                        .nome("Amanda")
                        .build()
                )
                .build()));

        VisualizarIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("1554asdf", outputData.getCodigo());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
        Assert.assertEquals("31415", outputData.getNumProcesso());
        Assert.assertEquals("TESTE", outputData.getFornecedor());
        Assert.assertEquals("7", outputData.getNota());
        Assert.assertEquals(BigDecimal.valueOf(7), outputData.getValorNota());
        Assert.assertEquals("ORGAO - orgao", outputData.getOrgao());
        Assert.assertEquals("SETOR - setor", outputData.getSetor());
        Assert.assertEquals("convenio", outputData.getConvenio());
        Assert.assertEquals("FUNDO - Fundo", outputData.getFundo());
        Assert.assertEquals("Projeto Teste", outputData.getProjeto());
        Assert.assertEquals("Amanda", outputData.getComodante());
        Assert.assertEquals("reconhecimento", outputData.getReconhecimento());
        Assert.assertEquals(DateValidate.formatarData(dataHoraRecebimento), outputData.getDataRecebimento());
        Assert.assertEquals(DateValidate.formatarData(dataNota), outputData.getDataNota());
        Assert.assertEquals("2020NL123456", outputData.getNumeroNotaLancamentoContabil());
        Assert.assertEquals(DateValidate.formatarData(dataNotaLancamentoContabil), outputData.getDataNotaLancamentoContabil());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdEhNulo() {
        VisualizarIncorporacaoInputData inputData = VisualizarIncorporacaoInputData
            .builder()
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoIncorporacaoNaoEncontrada() {
        VisualizarIncorporacaoInputData inputData = VisualizarIncorporacaoInputData
            .builder()
            .id(3L)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test
    public void deveIndicarQueIncorporacaoPossuiPatrimoniosIncorporadosEQueJaEstaoEmOutrasMovimentacoes() {
        final VisualizarIncorporacaoInputData inputData = VisualizarIncorporacaoInputData
            .builder()
            .id(1L)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.FINALIZADO)
                .build()));

        Mockito.when(patrimonioDataProvider.buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(anyLong()))
            .thenReturn(1L);

        final VisualizarIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getPossuiPatrimoniosEmOutrasMovimentacoes());
    }

    @Test
    public void deveIndicarQueIncorporacaoNaoPossuiPatrimoniosIncorporadosEQueJaEstaoEmOutrasMovimentacoes() {
        final VisualizarIncorporacaoInputData inputData = VisualizarIncorporacaoInputData
            .builder()
            .id(1L)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.FINALIZADO)
                .build()));

        Mockito.when(patrimonioDataProvider.buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(anyLong()))
            .thenReturn(0L);

        final VisualizarIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertFalse(outputData.getPossuiPatrimoniosEmOutrasMovimentacoes());
    }
}
