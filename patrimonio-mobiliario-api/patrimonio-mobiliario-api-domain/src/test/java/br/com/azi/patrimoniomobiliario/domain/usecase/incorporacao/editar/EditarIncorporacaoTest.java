package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.editar;

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
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.converter.EditarIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception.IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarIncorporacaoTest {

    private static final LocalDateTime dataHoraRecebimento = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
    private static final LocalDateTime dataNota = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
    private static final LocalDateTime dataNotaContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Captor
    private ArgumentCaptor<Incorporacao> incorporacaoArgumentCaptor;

    @Captor
    private ArgumentCaptor<NotaLancamentoContabil> notaLancamentoContabilArgumentCaptor;

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoIncorporacaoNaoExistir() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(4L)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverEmElaboracaoOuComErroProcessamento() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter()
        );

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData.builder()
            .id(1L)
            .build();

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
            .build();

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));

        useCase.executar(inputData);
    }

    @Test
    public void deveEditarTodosOsAtributosDaIncorporacao() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .codigo("15f1")
            .dataRecebimento(Date.from(dataHoraRecebimento.atZone(ZoneId.systemDefault()).toInstant()))
            .numProcesso("0898")
            .nota("8.6")
            .valorNota(BigDecimal.valueOf(8.6))
            .dataNota(Date.from(dataNota.atZone(ZoneId.systemDefault()).toInstant()))
            .fornecedor(1L)
            .orgao(3L)
            .setor(4L)
            .fundo(5L)
            .convenio(1L)
            .projeto("Projeto Teste")
            .comodante(1L)
            .usuario(5L)
            .reconhecimento(1L)
            .empenho(7L)
            .naturezaDespesa(2L)
            .origemConvenio(true)
            .origemFundo(true)
            .origemProjeto(true)
            .origemComodato(true)
            .numeroNotaLancamentoContabil("123")
            .dataNotaLancamentoContabil(Date.from(dataNotaContabil.atZone(ZoneId.systemDefault()).toInstant()))
            .build();

        final Incorporacao incorporacaoExistente = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .notaLancamentoContabil(NotaLancamentoContabil
                .builder()
                .numero("123")
                .dataLancamento(LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10))
                .build())
            .build();

        final Incorporacao incorporacaoParaSalvar = Incorporacao
            .builder()
            .id(2L)
            .reconhecimento(Reconhecimento.builder().id(1L).build())
            .fornecedor(Fornecedor.builder().id(1L).build())
            .orgao(UnidadeOrganizacional.builder().id(3L).build())
            .setor(UnidadeOrganizacional.builder().id(4L).build())
            .dataRecebimento(dataHoraRecebimento)
            .dataNota(dataNota)
            .convenio(Convenio.builder().id(1L).build())
            .fundo(UnidadeOrganizacional.builder().id(5L).build())
            .projeto("Projeto Teste")
            .comodante(Comodante.builder().id(1L).build())
            .empenho(Empenho.builder().id(7L).build())
            .naturezaDespesa(NaturezaDespesa.builder().id(2L).build())
            .usuario(Usuario.builder().id(5L).build())
            .codigo("15f1")
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .numProcesso("0898")
            .nota("8.6")
            .valorNota(BigDecimal.valueOf(8.6))
            .origemConvenio(true)
            .origemFundo(true)
            .origemProjeto(true)
            .origemComodato(true)
            .notaLancamentoContabil(
                NotaLancamentoContabil
                    .builder()
                    .numero("123")
                    .dataLancamento(LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10))
                    .build())
            .build();

        final Incorporacao incorporacaoAtualizada = Incorporacao
            .builder()
            .id(2L)
            .codigo("15f1")
            .dataRecebimento(dataHoraRecebimento)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .numProcesso("0898")
            .fornecedor(
                Fornecedor.builder()
                    .id(1L)
                    .cpfCnpj("12345678912345")
                    .nome("TESTE")
                    .situacao(Fornecedor.Situacao.ATIVO)
                    .build()
            )
            .nota("8.6")
            .valorNota(BigDecimal.valueOf(8.6))
            .dataNota(dataNota)
            .orgao(
                UnidadeOrganizacional
                    .builder()
                    .id(3L)
                    .nome("Unidade organizacional teste")
                    .sigla("UOT")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build()
            )
            .setor(
                UnidadeOrganizacional
                    .builder()
                    .id(4L)
                    .nome("Unidade organizacional setor teste")
                    .sigla("UOST")
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
            .convenio(
                Convenio
                    .builder()
                    .id(1L)
                    .numero("Convenio teste")
                    .build()
            )
            .fundo(UnidadeOrganizacional
                .builder()
                .id(5L)
                .nome("Unidade organizacional setor teste fundo")
                .sigla("UOSTF")
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .projeto("Projeto Teste")
            .comodante(Comodante.builder().id(1L).nome("Alberto").build())
            .usuario(
                Usuario
                    .builder()
                    .id(5L)
                    .nome("Joao")
                    .build()
            )
            .reconhecimento(
                Reconhecimento
                    .builder()
                    .id(1L)
                    .nome("Reconhecimento teste")
                    .situacao(Reconhecimento.Situacao.ATIVO)
                    .execucaoOrcamentaria(true)
                    .empenho(true)
                    .notaFiscal(true)
                    .build()
            )
            .empenho(
                Empenho
                    .builder()
                    .id(7L)
                    .dataEmpenho(LocalDateTime.now())
                    .build()
            )
            .naturezaDespesa(
                NaturezaDespesa
                    .builder()
                    .id(2L)
                    .despesa("Despesa nome")
                    .situacao(NaturezaDespesa.Situacao.ATIVO)
                    .build()
            )
            .origemConvenio(true)
            .origemFundo(true)
            .origemProjeto(true)
            .origemComodato(true)
            .notaLancamentoContabil(
                NotaLancamentoContabil
                    .builder()
                    .numero("234")
                    .dataLancamento(dataNotaContabil)
                    .build())
            .build();

        Mockito.when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class))).thenReturn(
            NotaLancamentoContabil
                .builder()
                .numero("123")
                .dataLancamento(LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10))
                .build());

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(incorporacaoAtualizada);

        final EditarIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(2), outputData.getId());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
        Assert.assertEquals(DateValidate.formatarData(dataHoraRecebimento), outputData.getDataRecebimento());
        Assert.assertEquals(DateValidate.formatarData(dataNota), outputData.getDataNota());
        Assert.assertEquals("15f1", outputData.getCodigo());
        Assert.assertEquals("0898", outputData.getNumProcesso());
        Assert.assertEquals("8.6", outputData.getNota());
        Assert.assertEquals(BigDecimal.valueOf(8.6), outputData.getValorNota());
        Assert.assertEquals(Long.valueOf(1), outputData.getConvenio());
        Assert.assertEquals(Long.valueOf(5), outputData.getFundo());
        Assert.assertEquals("Projeto Teste", outputData.getProjeto());
        Assert.assertEquals(Long.valueOf(7), outputData.getEmpenho());
        Assert.assertEquals(Long.valueOf(2), outputData.getNaturezaDespesa());
        Assert.assertEquals(true, outputData.getOrigemConvenio());
        Assert.assertEquals(true, outputData.getOrigemFundo());
        Assert.assertEquals(true, outputData.getOrigemProjeto());
        Assert.assertEquals(DateValidate.formatarData(dataNotaContabil), outputData.getDataNotaLancamentoContabil());
        Assert.assertEquals("234", outputData.getNumeroNotaLancamentoContabil());

        Assert.assertEquals(Long.valueOf(1), outputData.getReconhecimento().getId());
        Assert.assertEquals("Reconhecimento teste", outputData.getReconhecimento().getNome());
        Assert.assertEquals("ATIVO", outputData.getReconhecimento().getSituacao().toString());
        Assert.assertEquals(true, outputData.getReconhecimento().getExecucaoOrcamentaria());
        Assert.assertEquals(true, outputData.getReconhecimento().getNotaFiscal());
        Assert.assertEquals(true, outputData.getReconhecimento().getEmpenho());

        Assert.assertEquals(Long.valueOf(1), outputData.getFornecedor().getId());
        Assert.assertEquals("TESTE", outputData.getFornecedor().getNome());
        Assert.assertEquals("12345678912345", outputData.getFornecedor().getCpfCnpj());
        Assert.assertEquals("ATIVO", outputData.getFornecedor().getSituacao().toString());

        Assert.assertEquals(Long.valueOf(3), outputData.getOrgao().getId());
        Assert.assertEquals("UOT - Unidade organizacional teste", outputData.getOrgao().getDescricao());
        Assert.assertEquals("ATIVO", outputData.getOrgao().getSituacao().toString());

        Assert.assertEquals(Long.valueOf(4), outputData.getSetor().getId());
        Assert.assertEquals("UOST - Unidade organizacional setor teste", outputData.getSetor().getDescricao());
        Assert.assertEquals("ATIVO", outputData.getSetor().getSituacao().toString());

        Assert.assertEquals(true, outputData.getOrigemComodato());
        Assert.assertEquals(Long.valueOf(1), outputData.getComodante().getId());
        Assert.assertEquals("Alberto", outputData.getComodante().getNome());


        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoParaSalvar);
    }

    @Test
    public void devePermitirEditarIncorporacaoQuandoSituacaoForErroProcessamento() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .numProcesso("0898")
            .build();

        final Incorporacao incorporacaoExistente = Incorporacao.builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
            .build();

        final Incorporacao incorporacaoAtualizada = Incorporacao.builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
            .numProcesso("0898")
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(incorporacaoAtualizada);

        useCase.executar(inputData);

        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoAtualizada);
    }

    @Test
    public void deveRemoverConvenioFundoEProjetoQuandoSetarOrigemFalse() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .convenio(1L)
            .fundo(2L)
            .projeto("Projeto Teste")
            .origemConvenio(false)
            .origemFundo(false)
            .origemProjeto(false)
            .build();

        final Incorporacao incorporacaoExistente = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .convenio(Convenio.builder().id(1L).build())
            .fundo(UnidadeOrganizacional.builder().id(5L).build())
            .projeto("Projeto Teste")
            .origemConvenio(true)
            .origemFundo(true)
            .origemProjeto(true)
            .build();

        final Incorporacao incorporacaoParaSalvar = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .convenio(null)
            .fundo(null)
            .projeto(null)
            .origemConvenio(false)
            .origemFundo(false)
            .origemProjeto(false)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(new Incorporacao());

        useCase.executar(inputData);

        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoParaSalvar);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .codigo("15f1")
            .numProcesso("0898")
            .fornecedor(1L)
            .nota("8.6")
            .valorNota(BigDecimal.valueOf(8.6))
            .dataNota(Date.from(Instant.now().atZone(ZoneId.systemDefault()).withMonth(8).withDayOfMonth(3).toInstant()))
            .orgao(3L)
            .convenio(1L)
            .usuario(5L)
            .reconhecimento(1L)
            .empenho(7L)
            .naturezaDespesa(2L)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveInserirUmaNotaLancamentoContabilParaAIncorporacaoQuandoAindaNaoExistir() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final String numeroNotaLancamento = "2020NL123456";
        final EditarIncorporacaoInputData inputData = new EditarIncorporacaoInputData();
        inputData.setId(2L);
        inputData.setNumeroNotaLancamentoContabil(numeroNotaLancamento);

        final Incorporacao incorporacaoExistente = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .build();

        final NotaLancamentoContabil notaLancamentoSalva = NotaLancamentoContabil
            .builder()
            .id(1L)
            .numero(numeroNotaLancamento)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(Incorporacao.builder()
                .notaLancamentoContabil(NotaLancamentoContabil.builder()
                    .numero(numeroNotaLancamento)
                    .build())
                .build());

        Mockito.when(notaLancamentoContabilDataProvider.existePorNumero(anyString()))
            .thenReturn(false);

        Mockito.when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoSalva);

        EditarIncorporacaoOutputData outputData = useCase.executar(inputData);

        final InOrder inOrder = Mockito.inOrder(incorporacaoDataProvider, notaLancamentoContabilDataProvider);

        inOrder.verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero(numeroNotaLancamento);

        inOrder.verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilArgumentCaptor.capture());
        Assert.assertNull(notaLancamentoContabilArgumentCaptor.getValue().getId());
        Assert.assertEquals(numeroNotaLancamento, notaLancamentoContabilArgumentCaptor.getValue().getNumero());

        inOrder.verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoArgumentCaptor.capture());
        Assert.assertNotNull(incorporacaoArgumentCaptor.getValue().getNotaLancamentoContabil());
        Assert.assertEquals(notaLancamentoSalva.getId(), incorporacaoArgumentCaptor.getValue().getNotaLancamentoContabil().getId());

        Assert.assertEquals(numeroNotaLancamento, outputData.getNumeroNotaLancamentoContabil());
        Assert.assertNull(outputData.getDataNotaLancamentoContabil());
    }

    @Test
    public void deveAtualizarANotaLancamentoContabilVinculadaComIncorporacaoQuandoOsValoresMudarem() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final String numeroNotaLancamento = "2020NL123456";
        final String novoNumeroNotaLancamento = "2020NL123445";
        final Long idNotaLancamento = 1L;

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .numeroNotaLancamentoContabil(novoNumeroNotaLancamento)
            .dataNotaLancamentoContabil(Date.from(dataNotaContabil.atZone(ZoneId.systemDefault()).toInstant()))
            .build();

        final NotaLancamentoContabil notaLancamentoContabil = NotaLancamentoContabil
            .builder()
            .id(idNotaLancamento)
            .numero(numeroNotaLancamento)
            .dataLancamento(dataNotaContabil)
            .build();

        final Incorporacao incorporacaoExistente = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .notaLancamentoContabil(notaLancamentoContabil)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(Incorporacao.builder()
                .notaLancamentoContabil(NotaLancamentoContabil.builder()
                    .numero(novoNumeroNotaLancamento)
                    .build())
                .build());

        Mockito.when(notaLancamentoContabilDataProvider.existePorNumero(anyString()))
            .thenReturn(false);

        Mockito.when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabil);

        EditarIncorporacaoOutputData outputData = useCase.executar(inputData);

        final InOrder inOrder = Mockito.inOrder(incorporacaoDataProvider, notaLancamentoContabilDataProvider);

        inOrder.verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero(novoNumeroNotaLancamento);

        inOrder.verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilArgumentCaptor.capture());
        Assert.assertEquals(idNotaLancamento, notaLancamentoContabilArgumentCaptor.getValue().getId());
        Assert.assertEquals(novoNumeroNotaLancamento, notaLancamentoContabilArgumentCaptor.getValue().getNumero());
        Assert.assertEquals(dataNotaContabil, notaLancamentoContabilArgumentCaptor.getValue().getDataLancamento());

        inOrder.verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoArgumentCaptor.capture());
        Assert.assertNotNull(incorporacaoArgumentCaptor.getValue().getNotaLancamentoContabil());
        Assert.assertEquals(notaLancamentoContabil.getId(), incorporacaoArgumentCaptor.getValue().getNotaLancamentoContabil().getId());

        Assert.assertEquals(novoNumeroNotaLancamento, outputData.getNumeroNotaLancamentoContabil());
        Assert.assertNull(outputData.getDataNotaLancamentoContabil());
    }

    @Test
    public void devePermitirApagarAsInformacoesDeNotaLancamentoContabil() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final String numeroNotaLancamento = "2020NL123456";
        final Long idNotaLancamento = 1L;

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .numeroNotaLancamentoContabil(null)
            .dataNotaLancamentoContabil(null)
            .build();

        final NotaLancamentoContabil notaLancamentoContabil = NotaLancamentoContabil
            .builder()
            .id(idNotaLancamento)
            .numero(numeroNotaLancamento)
            .dataLancamento(dataNotaContabil)
            .build();

        final Incorporacao incorporacaoExistente = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .notaLancamentoContabil(notaLancamentoContabil)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(new Incorporacao());

        Mockito.when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabil);

        EditarIncorporacaoOutputData outputData = useCase.executar(inputData);

        final InOrder inOrder = Mockito.inOrder(incorporacaoDataProvider, notaLancamentoContabilDataProvider);

        inOrder.verify(notaLancamentoContabilDataProvider, times(0)).existePorNumero(anyString());

        inOrder.verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilArgumentCaptor.capture());
        Assert.assertEquals(idNotaLancamento, notaLancamentoContabilArgumentCaptor.getValue().getId());
        Assert.assertNull(notaLancamentoContabilArgumentCaptor.getValue().getNumero());
        Assert.assertNull(notaLancamentoContabilArgumentCaptor.getValue().getDataLancamento());

        inOrder.verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoArgumentCaptor.capture());
        Assert.assertNotNull(incorporacaoArgumentCaptor.getValue().getNotaLancamentoContabil());
        Assert.assertEquals(notaLancamentoContabil.getId(), incorporacaoArgumentCaptor.getValue().getNotaLancamentoContabil().getId());

        Assert.assertNull(outputData.getNumeroNotaLancamentoContabil());
        Assert.assertNull(outputData.getDataNotaLancamentoContabil());
    }

    @Test
    public void naoDeveInserirNotaLancamentoContabilQuandoAsInformacoesForemNulas() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .numeroNotaLancamentoContabil(null)
            .dataNotaLancamentoContabil(null)
            .build();

        final Incorporacao incorporacaoExistente = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .notaLancamentoContabil(null)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(new Incorporacao());

        EditarIncorporacaoOutputData outputData = useCase.executar(inputData);

        verifyZeroInteractions(notaLancamentoContabilDataProvider);
        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoArgumentCaptor.capture());
        Assert.assertNull(incorporacaoArgumentCaptor.getValue().getNotaLancamentoContabil());

        Assert.assertNull(outputData.getNumeroNotaLancamentoContabil());
        Assert.assertNull(outputData.getDataNotaLancamentoContabil());
    }

    @Test(expected = IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException.class)
    public void deveLancarExcecaoQuandoONumeroDaNotaLancamentoContabilEstiverDuplicado() {
        final EditarIncorporacaoUseCase useCase = new EditarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarIncorporacaoOutputDataConverter());

        final String numeroNotaLancamento = "2020NL123456";
        final String novoNumeroNotaLancamento = "2020NL123445";
        final Long idNotaLancamento = 1L;

        final EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .numeroNotaLancamentoContabil(novoNumeroNotaLancamento)
            .build();

        final NotaLancamentoContabil notaLancamentoContabil = NotaLancamentoContabil
            .builder()
            .id(idNotaLancamento)
            .numero(numeroNotaLancamento)
            .dataLancamento(dataNotaContabil)
            .build();

        final Incorporacao incorporacaoExistente = Incorporacao
            .builder()
            .id(2L)
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .notaLancamentoContabil(notaLancamentoContabil)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        Mockito.when(notaLancamentoContabilDataProvider.existePorNumero(anyString()))
            .thenReturn(true);

        useCase.executar(inputData);
    }
}
