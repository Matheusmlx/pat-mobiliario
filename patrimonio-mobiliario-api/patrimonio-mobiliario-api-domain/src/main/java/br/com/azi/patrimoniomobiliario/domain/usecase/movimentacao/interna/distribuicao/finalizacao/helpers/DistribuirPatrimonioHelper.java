package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PatrimonioEstornadoException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
public class DistribuirPatrimonioHelper {

    private final Clock clock;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    private final boolean patrimonioParaContaAlmoxarifado;

    public void distribuirPatrimonio(MovimentacaoItem movimentacaoItem, Movimentacao distribuicao, ContaContabil contaAlmoxarifado) {
        final Patrimonio patrimonio = buscarPatrimonio(movimentacaoItem.getPatrimonio().getId());

        atualizarSetor(patrimonio, distribuicao);
        atualizarContaContabil(patrimonio);
        atualizarDepreciavel(patrimonio);
        iniciarVidaUtilPatrimonio(patrimonio);
        salvarAlteracoes(patrimonio);

        gerarLancamentoContabil(patrimonio, distribuicao, contaAlmoxarifado);
    }

    private void atualizarSetor(Patrimonio patrimonio, Movimentacao distribuicao) {
        patrimonio.setSetor(distribuicao.getSetorDestino());
    }

    private void atualizarContaContabil(Patrimonio patrimonio) {
        if (patrimonioParaContaAlmoxarifado) {
            patrimonio.setContaContabilAtual(patrimonio.getContaContabilClassificacao());
        }
    }

    private void atualizarDepreciavel(Patrimonio patrimonio) {
        patrimonio.setDepreciavel(patrimonio.getItemIncorporacao().getConfiguracaoDepreciacao().getDepreciavel());
    }

    private Patrimonio buscarPatrimonio(Long id) {
        return patrimonioDataProvider.buscarPorId(id)
            .orElseThrow(() -> new PatrimonioNaoEncontradoException(id));
    }

    private void iniciarVidaUtilPatrimonio(Patrimonio patrimonio) {
        final boolean depreciavel = Boolean.TRUE.equals(patrimonio.getDepreciavel());
        final boolean primeiraDistribuicao = !movimentacaoItemDataProvider.existeDistribuicaoFinalizadaParaPatrimonio(patrimonio.getId());

        if (primeiraDistribuicao && depreciavel) {
            patrimonio.setInicioVidaUtil(LocalDateTime.now(clock));
            ConfiguracaoDepreciacao configuracaoDepreciacao = buscarConfiguracaoDepreciacao(patrimonio);

            patrimonio.setFimVidaUtil(LocalDateTime
                .from(patrimonio.getInicioVidaUtil())
                .plusMonths((long)configuracaoDepreciacao.getVidaUtil() - 1)
                .toLocalDate().atTime(23, 59, 59)
                .minusDays(1));
        }
    }

    private ConfiguracaoDepreciacao buscarConfiguracaoDepreciacao(Patrimonio patrimonio) {
        Optional<ConfiguracaoDepreciacao> configuracaoDepreciacao = configuracaoDepreciacaoDataProvider
            .buscarPorId(patrimonio.getItemIncorporacao().getConfiguracaoDepreciacao().getId());

        return configuracaoDepreciacao.orElseThrow(PatrimonioEstornadoException::new);
    }

    private void salvarAlteracoes(Patrimonio patrimonio) {
        patrimonioDataProvider.atualizar(patrimonio);
    }

    private void gerarLancamentoContabil(Patrimonio patrimonio, Movimentacao distribuicao, ContaContabil contaAlmoxarifado) {
        lancamentoContabilDataProvider.registrarLancamentoContabil(definirContaContabilDebito(patrimonio, contaAlmoxarifado),
            patrimonio, distribuicao, distribuicao.getOrgaoOrigem(), distribuicao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO, LancamentoContabil.TipoLancamento.DEBITO);

        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilClassificacao(),
            patrimonio, distribuicao, distribuicao.getOrgaoDestino(), distribuicao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.DISTRIBUICAO, LancamentoContabil.TipoLancamento.CREDITO);
    }

    private ContaContabil definirContaContabilDebito(Patrimonio patrimonio, ContaContabil contaContabilAlmoxarifado) {
        if (patrimonioParaContaAlmoxarifado) {
            return contaContabilAlmoxarifado;
        } else {
            return patrimonio.getContaContabilClassificacao();
        }
    }
}
