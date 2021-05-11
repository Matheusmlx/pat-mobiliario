package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.converter.FinalizarDistribuicaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.ContaContabilAlmoxarifadoInativaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.ContaContabilNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.DistribuicaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.DistribuicaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.DistribuicaoNaoPossuiItensException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.MovimentacaoNaoEhDistribuicaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.OrgaoOrigemInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorDestinoDistribuicaoEhAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorDestinoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorOrigemDistribuicaoNaoEhAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorOrigemInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.DistribuirPatrimonioHelper;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarDistribuicaoUseCaseTest {

    private static final String CONTA_CONTABIL_ALMOXARIFADO = "1234";
    private static final Long LIMITE_REGISTROS_PROCESSAMENTO_SINCRONO = 1L;
    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private ContaContabilDataProvider contaContabilDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    @Mock
    private DistribuirPatrimonioHelper distribuirPatrimonioHelper;

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private FinalizarDistribuicaoUseCase useCase;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoMovimentacaoIdForNulo() {
        instanciarUseCase(false);

        useCase.executar(new FinalizarDistribuicaoInputData());
    }

    @Test(expected = DistribuicaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoForEncontradaPorId() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build());
    }

    @Test(expected = DistribuicaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEstiverEmElaboracao() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(movimentacaoId)
                    .situacao(Movimentacao.Situacao.FINALIZADO)
                    .build()));

        useCase.executar(FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build());
    }

    @Test(expected = MovimentacaoNaoEhDistribuicaoException.class)
    public void deveFalharQuandoMovimentacaoNaoForDoTipoDistribuicao() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.TEMPORARIA)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoOrigemInativoException.class)
    public void deveFalharQuandoOrgaoOrigemEstiverInativo() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                .build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));

        useCase.executar(inputData);
    }

    @Test(expected = SetorOrigemInativoException.class)
    public void deveFalharQuandoSetorOrigemEstiverInativo() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                .build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));

        useCase.executar(inputData);
    }

    @Test(expected = SetorDestinoInativoException.class)
    public void deveFalharQuandoSetorDestinoEstiverInativo() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                .build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));

        useCase.executar(inputData);
    }

    @Test(expected = SetorOrigemDistribuicaoNaoEhAlmoxarifadoException.class)
    public void deveFalharQuandoSetorOrigemNaoForAlmoxarifado() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));

        useCase.executar(inputData);
    }

    @Test(expected = SetorDestinoDistribuicaoEhAlmoxarifadoException.class)
    public void deveFalharQuandoSetorDestinoForAlmoxarifado() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.TRUE)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.TRUE)
                .build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));

        useCase.executar(inputData);
    }

    @Test(expected = ContaContabilNaoEncontradaException.class)
    public void deveFalharQuandoAContaContabilDeAlmoxarifadoNaoForEncontrada() {
        instanciarUseCase(true);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = criarMovimentacaoValida(movimentacaoId);

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(1L);
        when(contaContabilDataProvider.buscarPorCodigo(anyString())).thenReturn(Optional.empty());

        useCase.executar(inputData);

        verify(contaContabilDataProvider, times(1)).buscarPorCodigo(CONTA_CONTABIL_ALMOXARIFADO);
    }

    @Test(expected = ContaContabilAlmoxarifadoInativaException.class)
    public void deveFalharQuandoContaContabilAlmoxarifadoEstiverInativa() {
        instanciarUseCase(true);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = criarMovimentacaoValida(movimentacaoId);
        final ContaContabil contaContabil = ContaContabil.builder()
            .id(1L)
            .codigo(CONTA_CONTABIL_ALMOXARIFADO)
            .situacao(ContaContabil.Situacao.INATIVO)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(1L);
        when(contaContabilDataProvider.buscarPorCodigo(anyString())).thenReturn(Optional.of(contaContabil));

        useCase.executar(inputData);
    }

    @Test(expected = DistribuicaoNaoPossuiItensException.class)
    public void deveFalharQuandoDistribuicaoNaoPossuirItensVinculados() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicao = criarMovimentacaoValida(movimentacaoId);

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicao));
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(0L);

        useCase.executar(inputData);
    }

    @Test
    public void deveProcessarOsPatrimoniosDeFormaSincronaUtilizandoContaAlmoxarifadoEFinalizarADistribuicao() {
        instanciarUseCase(true);

        final Long movimentacaoId = 1L;
        final String usuarioFinalizacao = "Usuário";
        final LocalDateTime dataFinalizacao = LocalDateTime.of(2021, 1, 12, 0, 0);

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicaoEmElaboracao = criarMovimentacaoValida(movimentacaoId);
        distribuicaoEmElaboracao.setSituacao(Movimentacao.Situacao.EM_ELABORACAO);

        final Movimentacao distribuicaoFinalizada = criarMovimentacaoValida(movimentacaoId);
        distribuicaoFinalizada.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicaoFinalizada.setUsuarioFinalizacao(usuarioFinalizacao);
        distribuicaoFinalizada.setDataFinalizacao(dataFinalizacao);

        final ContaContabil contaContabil = ContaContabil.builder()
            .id(1L)
            .codigo(CONTA_CONTABIL_ALMOXARIFADO)
            .situacao(ContaContabil.Situacao.ATIVO)
            .build();

        final MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .patrimonio(Patrimonio.builder().id(1L).build())
            .movimentacao(distribuicaoEmElaboracao)
            .build();

        final FinalizarDistribuicaoOutputData outputDataEsperado = FinalizarDistribuicaoOutputData.builder()
            .id(movimentacaoId)
            .dataFinalizacao(DateValidate.formatarData(dataFinalizacao))
            .situacao("FINALIZADO")
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicaoEmElaboracao));
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoFinalizada);
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(1L);
        when(contaContabilDataProvider.buscarPorCodigo(anyString())).thenReturn(Optional.of(contaContabil));
        when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(List.of(movimentacaoItem));

        final FinalizarDistribuicaoOutputData outputData = useCase.executar(inputData);

        assertNotNull(outputData);
        assertEquals(outputDataEsperado, outputData);

        verify(distribuirPatrimonioHelper, times(1))
            .distribuirPatrimonio(movimentacaoItem, distribuicaoEmElaboracao, contaContabil);
        verify(movimentacaoDataProvider, times(1)).salvar(distribuicaoFinalizada);
    }

    @Test
    public void deveProcessarOsPatrimoniosDeFormaSincronaNaoUtilizandoContaAlmoxarifadoEFinalizarADistribuicao() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;
        final String usuarioFinalizacao = "Usuário";
        final LocalDateTime dataFinalizacao = LocalDateTime.of(2021, 1, 12, 0, 0);

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicaoEmElaboracao = criarMovimentacaoValida(movimentacaoId);
        distribuicaoEmElaboracao.setSituacao(Movimentacao.Situacao.EM_ELABORACAO);

        final Movimentacao distribuicaoFinalizada = criarMovimentacaoValida(movimentacaoId);
        distribuicaoFinalizada.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicaoFinalizada.setUsuarioFinalizacao(usuarioFinalizacao);
        distribuicaoFinalizada.setDataFinalizacao(dataFinalizacao);

        final ContaContabil contaContabil = null;

        final MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .patrimonio(Patrimonio.builder().id(1L).build())
            .movimentacao(distribuicaoEmElaboracao)
            .build();

        final FinalizarDistribuicaoOutputData outputDataEsperado = FinalizarDistribuicaoOutputData.builder()
            .id(movimentacaoId)
            .dataFinalizacao(DateValidate.formatarData(dataFinalizacao))
            .situacao("FINALIZADO")
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicaoEmElaboracao));
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoFinalizada);
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(1L);
        when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(List.of(movimentacaoItem));

        final FinalizarDistribuicaoOutputData outputData = useCase.executar(inputData);

        assertNotNull(outputData);
        assertEquals(outputDataEsperado, outputData);

        verify(distribuirPatrimonioHelper, times(1))
            .distribuirPatrimonio(movimentacaoItem, distribuicaoEmElaboracao, contaContabil);
        verify(movimentacaoDataProvider, times(1)).salvar(distribuicaoFinalizada);
    }

    @Test
    public void devePrepararADistribuicaoParaProcessamentoAssincronoQuandoNumeroDeItensExcederParametro() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;
        final String usuarioFinalizacao = "Usuário";
        final LocalDateTime dataFinalizacao = LocalDateTime.of(2021, 1, 12, 0, 0);

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicaoEmElaboracao = criarMovimentacaoValida(movimentacaoId);
        distribuicaoEmElaboracao.setSituacao(Movimentacao.Situacao.EM_ELABORACAO);

        final Movimentacao distribuicaoEmProcessamento = criarMovimentacaoValida(movimentacaoId);
        distribuicaoEmProcessamento.setSituacao(Movimentacao.Situacao.EM_PROCESSAMENTO);
        distribuicaoEmProcessamento.setUsuarioFinalizacao(usuarioFinalizacao);
        distribuicaoEmProcessamento.setDataFinalizacao(dataFinalizacao);
        distribuicaoEmProcessamento.setDataInicioProcessamento(dataFinalizacao);

        final FinalizarDistribuicaoOutputData outputDataEsperado = FinalizarDistribuicaoOutputData.builder()
            .id(movimentacaoId)
            .dataFinalizacao(DateValidate.formatarData(dataFinalizacao))
            .situacao("EM_PROCESSAMENTO")
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicaoEmElaboracao));
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoEmProcessamento);
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(2L);
        when(sessaoUsuarioDataProvider.get()).thenReturn(SessaoUsuario.builder().id(1L).build());

        final FinalizarDistribuicaoOutputData outputData = useCase.executar(inputData);

        assertNotNull(outputData);
        assertEquals(outputDataEsperado, outputData);

        verifyZeroInteractions(distribuirPatrimonioHelper);
        verify(movimentacaoDataProvider, times(1)).salvar(distribuicaoEmProcessamento);
    }

    @Test
    public void deveGerarNotificacaoQuandoIncorporacaoForFinalizadaDeFormaAssincrona() {
        instanciarUseCase(false);

        final Long movimentacaoId = 1L;
        final String usuarioFinalizacao = "Usuário";
        final LocalDateTime dataFinalizacao = LocalDateTime.of(2021, 1, 12, 0, 0);

        final FinalizarDistribuicaoInputData inputData = FinalizarDistribuicaoInputData.builder()
            .id(movimentacaoId)
            .build();

        final Movimentacao distribuicaoEmElaboracao = criarMovimentacaoValida(movimentacaoId);
        distribuicaoEmElaboracao.setSituacao(Movimentacao.Situacao.EM_ELABORACAO);

        final Movimentacao distribuicaoEmProcessamento = criarMovimentacaoValida(movimentacaoId);
        distribuicaoEmProcessamento.setSituacao(Movimentacao.Situacao.EM_PROCESSAMENTO);
        distribuicaoEmProcessamento.setUsuarioFinalizacao(usuarioFinalizacao);
        distribuicaoEmProcessamento.setDataFinalizacao(dataFinalizacao);
        distribuicaoEmProcessamento.setDataInicioProcessamento(dataFinalizacao);

        final SessaoUsuario sessaoUsuario = SessaoUsuario.builder().id(1L).build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(movimentacaoId)
            .assunto("Distribuição " + distribuicaoEmElaboracao.getCodigo())
            .mensagem("Em processamento")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(Usuario.builder().id(sessaoUsuario.getId()).build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(distribuicaoEmElaboracao));
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);
        when(sessaoUsuarioDataProvider.get()).thenReturn(sessaoUsuario);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoEmProcessamento);
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong())).thenReturn(2L);

        useCase.executar(inputData);

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    private void instanciarUseCase(boolean patrimonioParaContaAlmoxarifado) {
        useCase = new FinalizarDistribuicaoUseCaseImpl(
            fixedClock,
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            contaContabilDataProvider,
            sessaoUsuarioDataProvider,
            notificacaoDataProvider,
            patrimonioParaContaAlmoxarifado,
            CONTA_CONTABIL_ALMOXARIFADO,
            LIMITE_REGISTROS_PROCESSAMENTO_SINCRONO,
            distribuirPatrimonioHelper,
            new FinalizarDistribuicaoOutputDataConverter()
        );
    }

    private Movimentacao criarMovimentacaoValida(Long movimentacaoId) {
        return Movimentacao.builder()
            .id(movimentacaoId)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.TRUE)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .build();
    }
}
