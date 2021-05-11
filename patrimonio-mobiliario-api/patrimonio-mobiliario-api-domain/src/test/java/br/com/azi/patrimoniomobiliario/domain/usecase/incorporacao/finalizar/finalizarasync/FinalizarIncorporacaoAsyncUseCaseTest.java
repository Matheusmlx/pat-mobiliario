package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.ContaContabilAlmoxarifadoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers.IncorporarPatrimonioHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarIncorporacaoAsyncUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);
    private final static String CODIGO_CONTA_CONTABIL_ALMOXARIFADO = "1234";

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private IncorporarPatrimonioHelper incorporarPatrimonioHelper;

    @Mock
    private ContaContabilDataProvider contaContabilDataProvider;

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    @Mock
    private UsuarioDataProvider usuarioDataProvider;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private FinalizarIncorporacaoAsyncUseCase useCase;

    private void criarUseCase(Boolean patrimonioParaContaAlmoxarifado, String codigoContaContabilAlmoxarifado) {
        useCase = new FinalizarIncorporacaoAsyncUseCaseImpl(
            fixedClock,
            patrimonioDataProvider,
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            contaContabilDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            incorporarPatrimonioHelper,
            patrimonioParaContaAlmoxarifado,
            codigoContaContabilAlmoxarifado

        );
    }

    @Test
    public void deveFalharQuandoIncorporacaoNaoForEncontrada() {
        criarUseCase(true, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final Long incorporacaoId = 1L;
        final FinalizarIncorporacaoAsyncInputData inputData = new FinalizarIncorporacaoAsyncInputData(incorporacaoId);

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        exceptionRule.expect(IncorporacaoNaoEncontradaException.class);
        exceptionRule.expectMessage("A incorporação #1 não foi encontrada.");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoNaoEncontrarAContaContabilAlmoxarifadoPeloCodigo() {
        criarUseCase(true, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        when(contaContabilDataProvider.buscarPorCodigo(CODIGO_CONTA_CONTABIL_ALMOXARIFADO)).thenReturn(Optional.empty());

        exceptionRule.expect(ContaContabilAlmoxarifadoNaoEncontradaException.class);
        exceptionRule.expectMessage("A conta contábil de almoxarifado não foi encontrada.");

        useCase.executar(dadosCenarioSucesso.getInputData());
    }

    @Test
    public void deveChamarOHelperParaRealizarAIncorporacaoDeTodosPatrimonioParaContaAlmoxarifado() {
        criarUseCase(true, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(incorporarPatrimonioHelper, times(1)).incorporarPatrimonio(
            dadosCenarioSucesso.getPatrimonio(),
            dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado()
        );
    }

    @Test
    public void deveChamarOHelperParaRealizarAIncorporacaoDeTodosPatrimonioParaContaClassificacao() {
        criarUseCase(false, null);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(incorporarPatrimonioHelper, times(1)).incorporarPatrimonio(
            dadosCenarioSucesso.getPatrimonio(),
            dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(),
            null
        );
    }

    @Test
    public void deveAtualizarASituacaoIncorporacaoAposFinalizarOProcessamentoDosPatrimonios() {
        criarUseCase(false, null);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Incorporacao incorporacaoAtualizada = dadosCenarioSucesso.getIncorporacao();
        incorporacaoAtualizada.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoAtualizada);
    }

    @Test
    public void deveAtualizarNotificacaoQuandoProcessamentoFinalizarComSucesso() {
        criarUseCase(false, null);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Incorporacao incorporacaoAtualizada = dadosCenarioSucesso.getIncorporacao();
        incorporacaoAtualizada.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        final Usuario usuarioFinalizacao = Usuario.builder().id(1L).build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.INCORPORACAO)
            .origemId(incorporacaoAtualizada.getId())
            .assunto("Incorporação " + incorporacaoAtualizada.getCodigo())
            .mensagem("Finalizado")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuarioFinalizacao)
            .build();

        final Notificacao NotificacaoAntiga = Notificacao.builder()
            .origem(Notificacao.Origem.INCORPORACAO)
            .origemId(incorporacaoAtualizada.getId())
            .assunto("Incorporação " + incorporacaoAtualizada.getCodigo())
            .mensagem("Em elaboração")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.TRUE)
            .usuario(usuarioFinalizacao)
            .build();

        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);
        when(usuarioDataProvider.buscarUsuarioPorLogin(incorporacaoAtualizada.getUsuarioFinalizacao()))
            .thenReturn(usuarioFinalizacao);
        when(notificacaoDataProvider.buscarNotificacaoPorOrigemEOrigemId(anyString(), anyLong()))
            .thenReturn(NotificacaoAntiga);

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void deveGerarNotificacaoQuandoProcessamentoFinalizarComSucesso() {
        criarUseCase(false, null);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Incorporacao incorporacaoAtualizada = dadosCenarioSucesso.getIncorporacao();
        incorporacaoAtualizada.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        final Usuario usuarioFinalizacao = Usuario.builder().id(1L).build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.INCORPORACAO)
            .origemId(incorporacaoAtualizada.getId())
            .assunto("Incorporação " + incorporacaoAtualizada.getCodigo())
            .mensagem("Finalizado")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuarioFinalizacao)
            .build();

        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);
        when(usuarioDataProvider.buscarUsuarioPorLogin(incorporacaoAtualizada.getUsuarioFinalizacao()))
            .thenReturn(usuarioFinalizacao);

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void naoDeveGerarNotificacaoQuandoNaoEncontrarUsuarioFinalizacaoDaIncorporacao() {
        criarUseCase(false, null);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Incorporacao incorporacaoAtualizada = dadosCenarioSucesso.getIncorporacao();
        incorporacaoAtualizada.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);
        when(usuarioDataProvider.buscarUsuarioPorLogin(incorporacaoAtualizada.getUsuarioFinalizacao()))
            .thenReturn(null);

        useCase.executar(dadosCenarioSucesso.getInputData());

        verifyZeroInteractions(notificacaoDataProvider);
    }

    private DadosCenarioSucesso prepararCenarioProcessamentoPatrimonioSucesso() {
        final Long incorporacaoId = 1L;
        final FinalizarIncorporacaoAsyncInputData inputData = new FinalizarIncorporacaoAsyncInputData(incorporacaoId);

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(incorporacaoId)
            .orgao(UnidadeOrganizacional.builder().id(1L).build())
            .setor(UnidadeOrganizacional.builder().id(2L).build())
            .convenio(Convenio.builder().id(1L).build())
            .projeto("Projeto 1")
            .comodante(Comodante.builder().id(1L).build())
            .fundo(UnidadeOrganizacional.builder().id(3L).build())
            .usuarioFinalizacao("Usuario")
            .dataFinalizacao(LOCAL_DATE.atStartOfDay())
            .dataRecebimento(LOCAL_DATE.atStartOfDay())
            .build();

        final ItemIncorporacao itemIncorporacao = ItemIncorporacao.builder()
            .id(1L)
            .naturezaDespesa(NaturezaDespesa.builder().id(1L).build())
            .contaContabil(ContaContabil.builder().id(1L).build())
            .estadoConservacao(EstadoConservacao.builder().id(1L).build())
            .uriImagem("repo1:imagem1")
            .incorporacao(incorporacao)
            .build();

        final Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorAquisicao(new BigDecimal(100))
            .itemIncorporacao(itemIncorporacao)
            .build();

        final ContaContabil contaContabilAlmoxarifado = ContaContabil.builder()
            .id(2L)
            .codigo(CODIGO_CONTA_CONTABIL_ALMOXARIFADO)
            .situacao(ContaContabil.Situacao.ATIVO)
            .build();

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));
        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(anyLong()))
            .thenReturn(List.of(itemIncorporacao));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosPorIncorporacaoId(anyLong())).thenReturn(1L);
        when(patrimonioDataProvider.buscarPatrimoniosPorIncorporacao(any(Patrimonio.Filtro.class)))
            .thenReturn(List.of(patrimonio));

        when(contaContabilDataProvider.buscarPorCodigo(CODIGO_CONTA_CONTABIL_ALMOXARIFADO))
            .thenReturn(Optional.of(contaContabilAlmoxarifado));

        return DadosCenarioSucesso.builder()
            .inputData(inputData)
            .incorporacao(incorporacao)
            .itemIncorporacao(itemIncorporacao)
            .patrimonio(patrimonio)
            .contaContabilAlmoxarifado(contaContabilAlmoxarifado)
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosCenarioSucesso {
        private FinalizarIncorporacaoAsyncInputData inputData;
        private Incorporacao incorporacao;
        private ItemIncorporacao itemIncorporacao;
        private Patrimonio patrimonio;
        private ContaContabil contaContabilAlmoxarifado;
    }
}
