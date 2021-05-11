package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.converter.EnviarMovimentacaoTemporariaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception.SetorTipoAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception.SituacaoMovimentacaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception.TipoDeMovimentacaoException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnviarMovimentacaoTemporariaUseCaseTest {

    private final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 15);

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private EnviarMovimentacaoTemporariaUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new EnviarMovimentacaoTemporariaUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            fixedClock,
            new EnviarMovimentacaoTemporariaOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoNulo() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = SituacaoMovimentacaoException.class)
    public void deveFalharSeMovimentacaoNaoEstaEmElaboracao() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.FINALIZADO)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = TipoDeMovimentacaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEhTipoTemporaria() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoInativoException.class)
    public void deveFalharQuandoOrgaoInativo() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoSetorOrigemInativo() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        exceptionRule.expect(SetorInativoException.class);
        exceptionRule.expectMessage("Setor origem selecionado está inativo. Por favor, selecione outro setor para finalizar.");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoSetorDestinoInativo() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(15L)
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        exceptionRule.expect(SetorInativoException.class);
        exceptionRule.expectMessage("Setor destino selecionado está inativo. Por favor, selecione outro setor para finalizar.");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoSetorOrigemAlmoxarifado() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(true)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(15L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .build()
            ));

        exceptionRule.expect(SetorTipoAlmoxarifadoException.class);
        exceptionRule.expectMessage("Setor origem é almoxarifado!");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoSetorDestinoAlmoxarifado() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(false)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(15L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(true)
                            .build()
                    )
                    .build()
            ));

        exceptionRule.expect(SetorTipoAlmoxarifadoException.class);
        exceptionRule.expectMessage("Setor destino é almoxarifado!");

        useCase.executar(inputData);
    }

    @Test
    public void deveAtualizarInformacoesMovimentacaoQuandoEnviar() {
        final String usuarioFinalizacao = "Usuário";

        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        Movimentacao movimentacaoFinalizada = Movimentacao
            .builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.AGUARDANDO_DEVOLUCAO)
            .usuarioFinalizacao(usuarioFinalizacao)
            .dataEnvio(LocalDateTime.of(2021, 1, 15, 0, 0))
            .tipo(TipoMovimentacaoEnum.TEMPORARIA)
            .orgaoOrigem(
                UnidadeOrganizacional
                    .builder()
                    .id(13L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build()
            )
            .setorOrigem(
                UnidadeOrganizacional
                    .builder()
                    .id(14L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(false)
                    .build()
            )
            .setorDestino(
                UnidadeOrganizacional
                    .builder()
                    .id(15L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(false)
                    .build()
            )
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(false)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(15L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(false)
                            .build()
                    )
                    .build()
            ));

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).salvar(movimentacaoFinalizada);
    }

    @Test
    public void deveGerarLancamentosContabeisPatrimonios() {
        EnviarMovimentacaoTemporariaInputData inputData = new EnviarMovimentacaoTemporariaInputData(12L);

        ContaContabil contaContabil = ContaContabil.builder().id(1L).build();

        Patrimonio patrimonio =  Patrimonio.builder()
            .id(2L)
            .valorAquisicao(BigDecimal.valueOf(2000))
            .valorLiquido(BigDecimal.valueOf(2000))
            .contaContabilAtual(contaContabil)
            .build();
        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(100L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(105L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(false).build();
        UnidadeOrganizacional setorDestino =  UnidadeOrganizacional.builder().id(106L).situacao(UnidadeOrganizacional.Situacao.ATIVO) .almoxarifado(false).build();

        Movimentacao movimentacaoFinalizada = Movimentacao
            .builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.AGUARDANDO_DEVOLUCAO)
            .dataEnvio(LocalDateTime.of(2021, 1, 15, 0, 0))
            .tipo(TipoMovimentacaoEnum.TEMPORARIA)
            .orgaoOrigem(orgaoOrigem)
            .orgaoDestino(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        List<MovimentacaoItem> movimentacaoItens = List.of(
            MovimentacaoItem
                .builder()
                .patrimonio(patrimonio)
                .build());

        List<Long> patrimoniosId = List.of(2L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                    .orgaoOrigem(orgaoOrigem)
                    .orgaoDestino(orgaoOrigem)
                    .setorOrigem(setorOrigem)
                    .setorDestino(setorDestino)
                    .build()
            ));

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong()))
            .thenReturn(movimentacaoItens);

        when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(List.of(patrimonio));

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).salvar(movimentacaoFinalizada);
        verify(movimentacaoItemDataProvider, times(1)).buscarPorMovimentacaoId(12L);
        verify(patrimonioDataProvider, times(1)).buscarTodosPatrimonios(patrimoniosId);
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(
            contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoOrigem(),
            movimentacaoFinalizada.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.TEMPORARIA_IDA,
            LancamentoContabil.TipoLancamento.DEBITO);
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(
            contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoDestino(),
            movimentacaoFinalizada.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.TEMPORARIA_IDA,
            LancamentoContabil.TipoLancamento.CREDITO);
    }

}
