package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarPorId;

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
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.converter.BuscarIncorporacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarIncorporacaoPorIdTest {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Before
    public void inicializa() {
        incorporacaoDataProvider = Mockito.mock(IncorporacaoDataProvider.class);
    }

    @Test
    public void deveBuscarIncorporacaoPorId() {
        BuscarIncorporacaoPorIdUseCase useCase = new BuscarIncorporacaoPorIdUseCaseImpl(
            incorporacaoDataProvider,
            new BuscarIncorporacaoPorIdOutputDataConverter()
        );

        BuscarIncorporacaoPorIdInputData inputData = new BuscarIncorporacaoPorIdInputData();
        inputData.setId(1L);

        LocalDateTime dataHoraRecebimento = LocalDateTime.of(2020,Month.JULY, 18,10,10,10);
        LocalDateTime dataNota = LocalDateTime.of(2020,Month.JULY, 18,10,10,10);
        LocalDateTime dataNotaLancamentoContabil = LocalDateTime.of(2020,Month.JULY, 18,10,10,10);

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
                .convenio(Convenio.builder().id(10L).build())
                .projeto("Projeto Teste")
                .usuario(Usuario.builder().id(5L).build())
                .reconhecimento(Reconhecimento.builder()
                    .id(27L)
                    .nome("Doação")
                    .notaFiscal(true)
                    .execucaoOrcamentaria(true)
                    .empenho(false)
                    .situacao(Reconhecimento.Situacao.ATIVO)
                    .build())
                .empenho(Empenho.builder().id(2L).build())
                .naturezaDespesa(NaturezaDespesa.builder().id(7L).situacao(NaturezaDespesa.Situacao.ATIVO).build())
                .origemConvenio(true)
                .origemFundo(false)
                .origemProjeto(true)
                .notaLancamentoContabil(NotaLancamentoContabil
                    .builder()
                    .id(1L)
                    .numero("2020NL123456")
                    .dataLancamento(dataNotaLancamentoContabil)
                    .build())
                .fundo(UnidadeOrganizacional.builder().id(5L).build())
                .build()));

        BuscarIncorporacaoPorIdOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("1554asdf", outputData.getCodigo());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
        Assert.assertEquals("31415", outputData.getNumProcesso());
        Assert.assertEquals(Long.valueOf(1), outputData.getFornecedor().getId());
        Assert.assertEquals("7", outputData.getNota());
        Assert.assertEquals(BigDecimal.valueOf(7), outputData.getValorNota());
        Assert.assertEquals(Long.valueOf(50), outputData.getOrgao().getId());
        Assert.assertEquals("OR - Orgão", outputData.getOrgao().getDescricao());
        Assert.assertEquals("ATIVO", outputData.getOrgao().getSituacao().toString());
        Assert.assertEquals(Long.valueOf(10), outputData.getConvenio());
        Assert.assertEquals("Projeto Teste", outputData.getProjeto());
        Assert.assertEquals(Long.valueOf(27), outputData.getReconhecimento().getId());
        Assert.assertEquals("Doação", outputData.getReconhecimento().getNome());
        Assert.assertEquals("ATIVO", outputData.getReconhecimento().getSituacao().toString());
        Assert.assertEquals(true, outputData.getReconhecimento().getExecucaoOrcamentaria());
        Assert.assertEquals(true, outputData.getReconhecimento().getNotaFiscal());
        Assert.assertEquals(false, outputData.getReconhecimento().getEmpenho());
        Assert.assertEquals(Long.valueOf(2), outputData.getEmpenho());
        Assert.assertEquals(Long.valueOf(7), outputData.getNaturezaDespesa());
        Assert.assertEquals(DateValidate.formatarData(dataHoraRecebimento), outputData.getDataRecebimento());
        Assert.assertEquals(DateValidate.formatarData(dataNota), outputData.getDataNota());
        Assert.assertEquals(Long.valueOf(1), outputData.getSetor().getId());
        Assert.assertEquals("SE - Setor", outputData.getSetor().getDescricao());
        Assert.assertEquals("ATIVO", outputData.getSetor().getSituacao().toString());
        Assert.assertEquals(true, outputData.getOrigemConvenio());
        Assert.assertEquals(false, outputData.getOrigemFundo());
        Assert.assertEquals(true, outputData.getOrigemProjeto());
        Assert.assertEquals("2020NL123456", outputData.getNumeroNotaLancamentoContabil());
        Assert.assertEquals(DateValidate.formatarData(dataNotaLancamentoContabil), outputData.getDataNotaLancamentoContabil());
        Assert.assertEquals(Long.valueOf(5), outputData.getFundo());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdEhNulo() {
        BuscarIncorporacaoPorIdUseCase useCase = new BuscarIncorporacaoPorIdUseCaseImpl(
            incorporacaoDataProvider,
            new BuscarIncorporacaoPorIdOutputDataConverter()
        );

        BuscarIncorporacaoPorIdInputData inputData = BuscarIncorporacaoPorIdInputData
            .builder()
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoIncorporacaoNaoEncontrada() {
        BuscarIncorporacaoPorIdUseCase useCase = new BuscarIncorporacaoPorIdUseCaseImpl(
            incorporacaoDataProvider,
            new BuscarIncorporacaoPorIdOutputDataConverter()
        );

        BuscarIncorporacaoPorIdInputData inputData = BuscarIncorporacaoPorIdInputData
            .builder()
            .id(3L)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }
}
