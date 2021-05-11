package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;

import java.util.Optional;

public interface MovimentacaoDataProvider {
    Movimentacao salvar(Movimentacao movimentacao);

    ListaPaginada<Movimentacao> buscarMovimentacaoInternaPorFiltro(Movimentacao.Filtro filtro);

    String buscarUltimoCodigoCadastrado();

    Optional<Movimentacao> buscarPorId(Long id);

    boolean existe(Long id);

    void remover(Movimentacao movimentacao);

    void removerPorId(Long id);

    Optional<Movimentacao> buscarUltimoNumeroTermoResponsabilidade();
}
