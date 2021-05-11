package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MovimentacaoItemDataProvider {

    List<MovimentacaoItem> buscarPorPatrimonio(Long id);

    ListaPaginada<MovimentacaoItem> buscarPorMovimentacaoId(MovimentacaoItem.Filtro filtro);

    List<MovimentacaoItem> buscarPorMovimentacaoId(Long movimentacaoId);

    Optional<MovimentacaoItem> buscarPorMovimentacaoEPatrimonio(Long movimentacaoId, Long patrimonioId);

    MovimentacaoItem salvar(MovimentacaoItem movimentacaoItem);

    List<MovimentacaoItem> salvar(List<MovimentacaoItem> movimentacaoItens);

    void remover(MovimentacaoItem movimentacaoItem);

    void removerPorMovimentacao(Long id);

    Long buscarQuantidadeItensPorMovimentacaoId(Long movimentacaoId);

    boolean existePorMovimentacaoId(Long movimentacaoId);

    boolean aguardandoDevolucao(Long movimentacaoId, Long patrimonioId);

    List<MovimentacaoItem> buscarItensAguardandoDevolucao(Long movimentacaoId);

    Long buscarQuantidadeItensDevolvidos(Long movimentacaoId);

    boolean existeItemAguardandoDevolucaoPorMovimentacao(Long movimentacaoId);

    boolean existeDistribuicaoFinalizadaParaPatrimonio(Long patrimonioId);

    Optional<MovimentacaoItem> buscarUltimaMovimentacaoPatrimonioNoPeriodo(Long patrimonioId, Date dataInicial, Date dataFinal);

    Optional<MovimentacaoItem> buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(Long patrimonioId, TipoMovimentacaoEnum tipo, Date dataInicial, Date dataFinal);

    Optional<MovimentacaoItem> buscarUltimaMovimentacaoPatrimonioAntesDe(Long patrimonioId, Date dataReferencia);

    boolean existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(Long patrimonioId, Date dataInicial, Date dataFinal);
}
