package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter.BuscarIncorporacoesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter.BuscarIncorporacoesOutputDataConverter;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuscarIncorporaoesUseCaseTest {

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private BuscarIncorporacoesUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarIncorporacoesUseCaseImpl(
            incorporacaoDataProvider,
            sessaoUsuarioDataProvider,
            new BuscarIncorporacoesFiltroConverter(),
            new BuscarIncorporacoesOutputDataConverter()
        );
    }

    @Test
    public void deveBuscarIncorporacoes() {
        BuscarIncorporacoesInputData inputData = BuscarIncorporacoesInputData
            .builder()
            .page(1L)
            .size(10L)
            .conteudo("ABC")
            .sort("codigo")
            .direction("ASC")
            .build();

        Incorporacao.Filtro filtroEsperado = Incorporacao.Filtro.builder()
            .conteudo("ABC")
            .usuarioId(1L)
            .funcoes(List.of(
                PermissaoMobiliarioConstants.NORMAL.getDescription(),
                PermissaoMobiliarioConstants.CONSULTA.getDescription()
            ))
            .build();

        filtroEsperado.setPage(0L);
        filtroEsperado.setSize(10L);
        filtroEsperado.setSort("codigo");
        filtroEsperado.setDirection("ASC");

        LocalDateTime dataRecebimento = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
        LocalDateTime dataNota = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
        LocalDateTime dataNotaLancamentoContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);

        Mockito.when(sessaoUsuarioDataProvider.get()).thenReturn(SessaoUsuario.builder()
            .id(1L)
            .build());

        Mockito.when(incorporacaoDataProvider.buscarPorFiltro(any(Incorporacao.Filtro.class)))
            .thenReturn(ListaPaginada.<Incorporacao>builder()
                .items(List.of(
                    Incorporacao
                        .builder()
                        .id(2L)
                        .codigo("15f1")
                        .dataRecebimento(dataRecebimento)
                        .situacao(Incorporacao.Situacao.FINALIZADO)
                        .numProcesso("0898")
                        .justificativa("Criação")
                        .fornecedor(
                            Fornecedor.builder()
                                .id(1L)
                                .cpfCnpj("12345678912345")
                                .nome("TESTE")
                                .build())
                        .nota("8.6")
                        .valorNota(BigDecimal.valueOf(8.6))
                        .dataNota(dataNota)
                        .orgao(
                            UnidadeOrganizacional
                                .builder()
                                .id(3L)
                                .nome("Unidade organizacional teste")
                                .sigla("UOT")
                                .build())
                        .setor(
                            UnidadeOrganizacional
                                .builder()
                                .id(2L)
                                .nome("Unidade organizacional setor teste")
                                .sigla("UOST")
                                .build())
                        .convenio(
                            Convenio
                                .builder()
                                .id(1L)
                                .numero("Convenio teste")
                                .build())
                        .fundo(UnidadeOrganizacional.builder().id(3L).build())
                        .projeto("Projeto Teste")
                        .usuario(
                            Usuario
                                .builder()
                                .id(5L)
                                .nome("Joao")
                                .build())
                        .reconhecimento(
                            Reconhecimento
                                .builder()
                                .id(1L)
                                .nome("Reconhecimento teste")
                                .notaFiscal(true)
                                .empenho(true)
                                .build())
                        .origemConvenio(true)
                        .origemFundo(true)
                        .origemProjeto(true)
                        .notaLancamentoContabil(NotaLancamentoContabil
                            .builder()
                            .id(1L)
                            .numero("2020NL123456")
                            .dataLancamento(dataNotaLancamentoContabil)
                            .build())
                        .build()))
                .totalElements(1L)
                .totalPages(1L)
                .build());

        BuscarIncorporacoesOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals("15f1", outputData.getItems().get(0).getCodigo());
        Assert.assertEquals("FINALIZADO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals("TESTE", outputData.getItems().get(0).getFornecedor());
        Assert.assertEquals(java.util.Optional.of(2L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalElements());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalPages());
        Assert.assertEquals(DateValidate.formatarData(dataRecebimento), outputData.getItems().get(0).getDataRecebimento());
        Assert.assertEquals("UOT", outputData.getItems().get(0).getOrgao());
        Assert.assertEquals(DateValidate.formatarData(dataNota), outputData.getItems().get(0).getDataNota());
        Assert.assertEquals("0898", outputData.getItems().get(0).getNumProcesso());
        Assert.assertEquals("8.6", outputData.getItems().get(0).getNota());
        Assert.assertEquals(BigDecimal.valueOf(8.6), outputData.getItems().get(0).getValorNota());
        Assert.assertEquals(Long.valueOf(2), outputData.getItems().get(0).getSetor());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getConvenio());
        Assert.assertEquals(Long.valueOf(3), outputData.getItems().get(0).getFundo());
        Assert.assertEquals("Projeto Teste", outputData.getItems().get(0).getProjeto());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getReconhecimento().getId());
        Assert.assertEquals(true, outputData.getItems().get(0).getReconhecimento().getEmpenho());
        Assert.assertEquals(true, outputData.getItems().get(0).getReconhecimento().getNotaFiscal());
        Assert.assertEquals(true, outputData.getItems().get(0).getOrigemConvenio());
        Assert.assertEquals(true, outputData.getItems().get(0).getOrigemFundo());
        Assert.assertEquals(true, outputData.getItems().get(0).getOrigemProjeto());
        Assert.assertEquals("2020NL123456", outputData.getItems().get(0).getNumeroNotaLancamentoContabil());
        Assert.assertEquals(DateValidate.formatarData(dataNotaLancamentoContabil), outputData.getItems().get(0).getDataNotaLancamentoContabil());

        verify(sessaoUsuarioDataProvider, times(1)).get();
        verify(incorporacaoDataProvider, times(1)).buscarPorFiltro(filtroEsperado);
    }

}
