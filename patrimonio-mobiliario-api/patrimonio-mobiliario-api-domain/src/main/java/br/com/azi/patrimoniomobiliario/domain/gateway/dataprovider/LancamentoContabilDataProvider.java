package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import lombok.NonNull;

import java.util.List;

public interface LancamentoContabilDataProvider {
    LancamentoContabil salvar(LancamentoContabil lancamentoContabil);

    void removerPorPatrimonio(Patrimonio patrimonio);

    void removerPorMovimentacao(Long id);

    List<LancamentoContabil> buscarCreditoPorPatrimoniosEMovimentacao(List<Long> patrimoniosId, Long movimentacaoId);

    List<LancamentoContabil> buscarCreditoNaVoltaPorPatrimoniosEMovimentacao(List<Long> patrimoniosId, Long movimentacaoId);

    LancamentoContabil registrarLancamentoContabil(@NonNull ContaContabil contaContabilCredito,
                                                   @NonNull Patrimonio patrimonio,
                                                   @NonNull Movimentacao movimentacao,
                                                   @NonNull UnidadeOrganizacional orgaoCreditado,
                                                   @NonNull UnidadeOrganizacional setorCreditado,
                                                   @NonNull LancamentoContabil.TipoMovimentacao tipoMovimentacao,
                                                   @NonNull LancamentoContabil.TipoLancamento tipoLancamento);

}
