package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.ContaContabilAlmoxarifadoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.DistribuicaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.DistribuirPatrimonioHelper;
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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarDistribuicaoAsyncUseCaseTest {

    private static final String CODIGO_CONTA_CONTABIL_ALMOXARIFADO = "1234";
    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);
    private final Clock fixedClock = Clock.fixed(
        LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()
    );

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private ContaContabilDataProvider contaContabilDataProvider;

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    @Mock
    private UsuarioDataProvider usuarioDataProvider;

    @Mock
    private DistribuirPatrimonioHelper distribuirPatrimonioHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private FinalizarDistribuicaoAsyncUseCase useCase;

    public void construirUseCase(boolean patrimonioParaContaAlmoxarifado, String codigoContaContabilAlmoxarifado) {
        useCase = new FinalizarDistribuicaoAsyncUseCaseImpl(
            fixedClock,
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            contaContabilDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            distribuirPatrimonioHelper,
            patrimonioParaContaAlmoxarifado,
            codigoContaContabilAlmoxarifado
        );
    }

    @Test
    public void deveFalharQuandoNaoEncontrarAMovimentacaoPeloId() {
        construirUseCase(true, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final FinalizarDistribuicaoAsyncInputData inputData = new FinalizarDistribuicaoAsyncInputData(1L);

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        expectedException.expect(DistribuicaoNaoEncontradaException.class);
        expectedException.expectMessage("A distribuição com id #1 não foi encontrada.");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoNaoEncontrarAContaDeAlmoxarifadoPeloCodigo() {
        final boolean patrimonioParaContaAlmoxarifado = true;

        construirUseCase(patrimonioParaContaAlmoxarifado, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado);
        when(contaContabilDataProvider.buscarPorCodigo(CODIGO_CONTA_CONTABIL_ALMOXARIFADO)).thenReturn(Optional.empty());

        expectedException.expect(ContaContabilAlmoxarifadoNaoEncontradaException.class);
        expectedException.expectMessage("A conta contábil de almoxarifado não foi encontrada.");

        useCase.executar(dadosCenarioSucesso.getInputData());
    }

    @Test
    public void deveProcessarTodosItensDaDistribuicaoComParametroContaAlmoxarifadoNull() {
        final boolean patrimonioParaContaAlmoxarifado = false;

        construirUseCase(patrimonioParaContaAlmoxarifado, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado);

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(new Movimentacao());

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(distribuirPatrimonioHelper, times(1)).distribuirPatrimonio(
            dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(), null);
    }

    @Test
    public void deveProcessarTodosItensDaDistribuicaoComContaAlmoxarifadoDefinida() {
        final boolean patrimonioParaContaAlmoxarifado = true;

        construirUseCase(patrimonioParaContaAlmoxarifado, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado);

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(new Movimentacao());

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(distribuirPatrimonioHelper, times(1)).distribuirPatrimonio(
            dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado());
    }

    @Test
    public void deveAtualizarInformacoesDaDistribuicaoQuandoFinalizarOProcessamentoDosItens() {
        final boolean patrimonioParaContaAlmoxarifado = false;

        construirUseCase(patrimonioParaContaAlmoxarifado, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado);
        final Movimentacao distribuicaoAtualizada = dadosCenarioSucesso.getDistribuicao();
        distribuicaoAtualizada.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoAtualizada);

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(movimentacaoDataProvider, times(1)).salvar(distribuicaoAtualizada);
    }

    @Test
    public void deveAtualizarNotificacaoParaDistribuicaoProcessadaComSucesso() {
        final boolean patrimonioParaContaAlmoxarifado = false;

        construirUseCase(patrimonioParaContaAlmoxarifado, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado);
        final Movimentacao distribuicaoAtualizada = dadosCenarioSucesso.getDistribuicao();
        distribuicaoAtualizada.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        final Usuario usuario = Usuario.builder().id(1L).build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(distribuicaoAtualizada.getId())
            .assunto("Distribuição " + distribuicaoAtualizada.getCodigo())
            .mensagem("Finalizado")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuario)
            .build();

        final Notificacao notificacaoAntiga = Notificacao.builder()
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(distribuicaoAtualizada.getId())
            .assunto("Distribuição " + distribuicaoAtualizada.getCodigo())
            .mensagem("Em elaboração")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuario)
            .build();

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoAtualizada);

        when(usuarioDataProvider.buscarUsuarioPorLogin(dadosCenarioSucesso.getDistribuicao().getUsuarioFinalizacao()))
            .thenReturn(usuario);

        when(notificacaoDataProvider.buscarNotificacaoPorOrigemEOrigemId(anyString(), anyLong()))
            .thenReturn(notificacaoAntiga);

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void deveGerarNotificacaoParaDistribuicaoProcessadaComSucesso() {
        final boolean patrimonioParaContaAlmoxarifado = false;

        construirUseCase(patrimonioParaContaAlmoxarifado, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado);
        final Movimentacao distribuicaoAtualizada = dadosCenarioSucesso.getDistribuicao();
        distribuicaoAtualizada.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        final Usuario usuario = Usuario.builder().id(1L).build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(distribuicaoAtualizada.getId())
            .assunto("Distribuição " + distribuicaoAtualizada.getCodigo())
            .mensagem("Finalizado")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuario)
            .build();

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoAtualizada);

        when(usuarioDataProvider.buscarUsuarioPorLogin(dadosCenarioSucesso.getDistribuicao().getUsuarioFinalizacao()))
            .thenReturn(usuario);

        useCase.executar(dadosCenarioSucesso.getInputData());

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void naoDeveGerarNotificacaoParaDistribuicaoQuandoNaoEncontrarUsuarioFinalizacao() {
        final boolean patrimonioParaContaAlmoxarifado = false;

        construirUseCase(patrimonioParaContaAlmoxarifado, CODIGO_CONTA_CONTABIL_ALMOXARIFADO);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado);
        final Movimentacao distribuicaoAtualizada = dadosCenarioSucesso.getDistribuicao();
        distribuicaoAtualizada.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicaoAtualizada.setDataFimProcessamento(LOCAL_DATE.atStartOfDay());

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoAtualizada);

        when(usuarioDataProvider.buscarUsuarioPorLogin(dadosCenarioSucesso.getDistribuicao().getUsuarioFinalizacao()))
            .thenReturn(null);

        useCase.executar(dadosCenarioSucesso.getInputData());

        verifyZeroInteractions(notificacaoDataProvider);
    }

    private DadosCenarioSucesso prepararCenarioProcessamentoSucesso(boolean patrimonioParaContaAlmoxarifado) {
        final Long movimentacaoId = 1L;
        final FinalizarDistribuicaoAsyncInputData inputData = new FinalizarDistribuicaoAsyncInputData(1L);

        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(2L).build();
        UnidadeOrganizacional setorDestino = UnidadeOrganizacional.builder().id(4L).build();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(movimentacaoId)
            .orgaoOrigem(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .orgaoDestino(setorDestino)
            .setorDestino(orgaoOrigem)
            .situacao(Movimentacao.Situacao.EM_PROCESSAMENTO)
            .dataFinalizacao(LOCAL_DATE.atStartOfDay())
            .usuarioFinalizacao("Usuario")
            .build();

        final ContaContabil contaContabilAlmoxarifado = ContaContabil.builder()
            .id(1L)
            .codigo(CODIGO_CONTA_CONTABIL_ALMOXARIFADO)
            .situacao(ContaContabil.Situacao.ATIVO)
            .build();

        final ContaContabil contaContabilClassificacao = ContaContabil.builder()
            .build();

        final Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorAquisicao(new BigDecimal(100))
            .contaContabilAtual(patrimonioParaContaAlmoxarifado ? contaContabilAlmoxarifado : contaContabilClassificacao)
            .contaContabilClassificacao(contaContabilClassificacao)
            .build();

        final MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(distribuicao)
            .patrimonio(patrimonio)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(1L);
        when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(any(MovimentacaoItem.Filtro.class)))
            .thenReturn(ListaPaginada.<MovimentacaoItem>builder()
                .items(List.of(movimentacaoItem))
                .build()
            );
        when(contaContabilDataProvider.buscarPorCodigo(anyString())).thenReturn(Optional.of(contaContabilAlmoxarifado));

        return DadosCenarioSucesso.builder()
            .inputData(inputData)
            .distribuicao(distribuicao)
            .movimentacaoItem(movimentacaoItem)
            .contaContabilAlmoxarifado(contaContabilAlmoxarifado)
            .contaContabilClassificacao(contaContabilClassificacao)
            .patrimonio(patrimonio)
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosCenarioSucesso {
        private FinalizarDistribuicaoAsyncInputData inputData;
        private Movimentacao distribuicao;
        private MovimentacaoItem movimentacaoItem;
        private ContaContabil contaContabilAlmoxarifado;
        private ContaContabil contaContabilClassificacao;
        private Patrimonio patrimonio;
    }
}
