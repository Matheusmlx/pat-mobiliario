package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.FinalizarDistribuicaoAsyncInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.exception.PatrimonioNaoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DistribuirPatrimonioHelperTest {

    private static final String CODIGO_CONTA_CONTABIL_ALMOXARIFADO = "1234";
    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    private DistribuirPatrimonioHelper helper;

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarPatrimonio() {
        instanciarHelper(false);

        final Movimentacao distribuicao = Movimentacao.builder().build();

        final Patrimonio patrimonio = Patrimonio.builder().id(1L).build();

        final MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(distribuicao)
            .patrimonio(patrimonio)
            .build();

        when(patrimonioDataProvider.buscarPorId(patrimonio.getId())).thenReturn(Optional.empty());

        helper.distribuirPatrimonio(movimentacaoItem, distribuicao, null);
    }

    @Test
    public void deveAtualizarAsInformacoesDosPatrimoniosEAlterarContaContabilAtualParaContaContabilClassificacao() {
        final boolean patrimonioParaContaAlmoxarifado = true;
        final boolean patrimonioDepreciavel = false;

        instanciarHelper(patrimonioParaContaAlmoxarifado);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado, patrimonioDepreciavel);

        final Patrimonio patrimonioAtualizado = dadosCenarioSucesso.getPatrimonio();
        patrimonioAtualizado.setSetor(dadosCenarioSucesso.getDistribuicao().getSetorDestino());
        patrimonioAtualizado.setContaContabilAtual(dadosCenarioSucesso.getContaContabilClassificacao());

        helper.distribuirPatrimonio(dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonioAtualizado);
    }

    @Test
    public void deveAtualizarAsInformacoesDosPatrimoniosENaoAlterarContaContabilAtual() {
        final boolean patrimonioParaContaAlmoxarifado = false;
        final boolean patrimonioDepreciavel = false;

        instanciarHelper(patrimonioParaContaAlmoxarifado);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado, patrimonioDepreciavel);

        final Patrimonio patrimonioAtualizado = dadosCenarioSucesso.getPatrimonio();
        patrimonioAtualizado.setSetor(dadosCenarioSucesso.getDistribuicao().getSetorDestino());
        patrimonioAtualizado.setContaContabilAtual(dadosCenarioSucesso.getContaContabilClassificacao());

        helper.distribuirPatrimonio(dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonioAtualizado);
    }

    @Test
    public void deveGerarLancamentoContabilDebitoParaSetorOrigemNaContaContabilAlmoxarifado() {
        final boolean patrimonioParaContaAlmoxarifado = true;
        final boolean patrimonioDepreciavel = false;

        instanciarHelper(patrimonioParaContaAlmoxarifado);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado, patrimonioDepreciavel);
        ContaContabil contaContabilAlmoxarifado = dadosCenarioSucesso.getContaContabilAlmoxarifado();
        ContaContabil contaContabilClassificacao = dadosCenarioSucesso.getContaContabilClassificacao();
        Patrimonio patrimonio = dadosCenarioSucesso.getPatrimonio();
        Movimentacao movimentacao = dadosCenarioSucesso.getDistribuicao();

        helper.distribuirPatrimonio(dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado());

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabilAlmoxarifado,
            patrimonio,
            movimentacao,
            movimentacao.getOrgaoOrigem(),
            movimentacao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO,
            LancamentoContabil.TipoLancamento.DEBITO);

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabilClassificacao,
            patrimonio,
            movimentacao,
            movimentacao.getOrgaoDestino(),
            movimentacao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO,
            LancamentoContabil.TipoLancamento.CREDITO);
    }

    @Test
    public void deveGerarLancamentoContabilDebitoParaSetorOrigemNaContaContabilDeClassificacao() {
        final boolean patrimonioParaContaAlmoxarifado = false;
        final boolean patrimonioDepreciavel = false;

        instanciarHelper(patrimonioParaContaAlmoxarifado);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado, patrimonioDepreciavel);
        ContaContabil contaContabilClassificacao = dadosCenarioSucesso.getContaContabilClassificacao();
        Patrimonio patrimonio = dadosCenarioSucesso.getPatrimonio();
        Movimentacao movimentacao = dadosCenarioSucesso.getDistribuicao();

        helper.distribuirPatrimonio(dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado());

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabilClassificacao,
            patrimonio,
            movimentacao,
            movimentacao.getOrgaoOrigem(),
            movimentacao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO,
            LancamentoContabil.TipoLancamento.DEBITO);

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabilClassificacao,
            patrimonio,
            movimentacao,
            movimentacao.getOrgaoDestino(),
            movimentacao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO,
            LancamentoContabil.TipoLancamento.CREDITO);
    }

    @Test
    public void deveGerarLancamentoContabilCreditoParaSetorDestinoNaContaContabilDeClassificacao() {
        final boolean patrimonioParaContaAlmoxarifado = false;
        final boolean patrimonioDepreciavel = false;

        instanciarHelper(patrimonioParaContaAlmoxarifado);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado, patrimonioDepreciavel);
        ContaContabil contaContabilClassificacao = dadosCenarioSucesso.getContaContabilClassificacao();
        Patrimonio patrimonio = dadosCenarioSucesso.getPatrimonio();
        Movimentacao movimentacao = dadosCenarioSucesso.getDistribuicao();

        helper.distribuirPatrimonio(dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado());

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabilClassificacao,
            patrimonio,
            movimentacao,
            movimentacao.getOrgaoOrigem(),
            movimentacao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO,
            LancamentoContabil.TipoLancamento.DEBITO);

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabilClassificacao,
            patrimonio,
            movimentacao,
            movimentacao.getOrgaoDestino(),
            movimentacao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO,
            LancamentoContabil.TipoLancamento.CREDITO);
    }

    @Test
    public void deveIniciarVidaUtilPatrimonioQuandoForPrimeiraMovimentacaoPatrimonio() {
        final boolean patrimonioParaContaAlmoxarifado = false;
        final boolean patrimonioDepreciavel = true;
        final LocalDateTime vidaUtilInicio = LOCAL_DATE.atStartOfDay();
        final LocalDateTime vidaUtilFim = LocalDateTime
            .from(vidaUtilInicio)
            .plusMonths(12 - 1)
            .toLocalDate().atTime(23, 59, 59)
            .minusDays(1);

        instanciarHelper(patrimonioParaContaAlmoxarifado);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoSucesso(patrimonioParaContaAlmoxarifado, patrimonioDepreciavel);

        Patrimonio patrimonio = dadosCenarioSucesso.getPatrimonio();

        when(movimentacaoItemDataProvider.existeDistribuicaoFinalizadaParaPatrimonio(anyLong()))
            .thenReturn(false);

        when(configuracaoDepreciacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(ConfiguracaoDepreciacao
                .builder()
                .id(5L)
                .depreciavel(patrimonioDepreciavel)
                .percentualResidual(BigDecimal.valueOf(10))
                .vidaUtil(12)
                .build()));

        helper.distribuirPatrimonio(dadosCenarioSucesso.getMovimentacaoItem(), dadosCenarioSucesso.getDistribuicao(),
            dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonio);
        assertEquals(vidaUtilInicio, patrimonio.getInicioVidaUtil());
        assertEquals(vidaUtilFim, patrimonio.getFimVidaUtil());
    }

    private void instanciarHelper(boolean patrimonioParaContaAlmoxarifado) {
        final Clock clock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        helper = new DistribuirPatrimonioHelper(
            clock,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            movimentacaoItemDataProvider,
            configuracaoDepreciacaoDataProvider,
            patrimonioParaContaAlmoxarifado
        );
    }

    private DadosCenarioSucesso prepararCenarioProcessamentoSucesso(boolean patrimonioParaContaAlmoxarifado, boolean patrimonioDepreciavel) {
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

        final ItemIncorporacao itemIncorporacao = ItemIncorporacao
            .builder()
            .id(3L)
            .configuracaoDepreciacao(ConfiguracaoDepreciacao
                .builder()
                .id(5L)
                .depreciavel(patrimonioDepreciavel)
                .percentualResidual(BigDecimal.valueOf(10))
                .vidaUtil(12)
                .build())
            .build();

        final ContaContabil contaContabilAlmoxarifado = ContaContabil.builder()
            .id(1L)
            .codigo(CODIGO_CONTA_CONTABIL_ALMOXARIFADO)
            .situacao(ContaContabil.Situacao.ATIVO)
            .build();

        final ContaContabil contaContabilClassificacao = ContaContabil.builder()
            .id(2L)
            .situacao(ContaContabil.Situacao.ATIVO)
            .build();

        final Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorAquisicao(new BigDecimal(100))
            .contaContabilAtual(patrimonioParaContaAlmoxarifado ? contaContabilAlmoxarifado : contaContabilClassificacao)
            .contaContabilClassificacao(contaContabilClassificacao)
            .itemIncorporacao(itemIncorporacao)
            .depreciavel(patrimonioDepreciavel)
            .build();

        final MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(distribuicao)
            .patrimonio(patrimonio)
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(patrimonio));

        return DadosCenarioSucesso.builder()
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
        private Movimentacao distribuicao;
        private MovimentacaoItem movimentacaoItem;
        private ContaContabil contaContabilAlmoxarifado;
        private ContaContabil contaContabilClassificacao;
        private Patrimonio patrimonio;
    }

}
