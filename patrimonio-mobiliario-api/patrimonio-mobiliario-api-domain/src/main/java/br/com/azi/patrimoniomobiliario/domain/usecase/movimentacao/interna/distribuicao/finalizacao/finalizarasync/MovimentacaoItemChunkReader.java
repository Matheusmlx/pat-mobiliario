package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import lombok.Getter;

import java.util.List;

@Getter
public class MovimentacaoItemChunkReader {
    private static final Long CHUNK_SIZE = 10L;

    private Long page = 0L;
    private Long totalPages = 0L;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;
    private final Long movimentacaoId;

    public MovimentacaoItemChunkReader(final MovimentacaoItemDataProvider movimentacaoItemDataProvider, final Long movimentacaoId) {
        this.movimentacaoItemDataProvider = movimentacaoItemDataProvider;
        this.movimentacaoId = movimentacaoId;
        this.totalPages = this.calcTotalPages();
    }

    public boolean hasNext() {
        return this.page < this.totalPages;
    }

    public List<MovimentacaoItem> next() {
        final MovimentacaoItem.Filtro filtro = MovimentacaoItem.Filtro.builder()
            .movimentacaoId(movimentacaoId)
            .build();

        filtro.setPage(this.page);
        filtro.setSize(CHUNK_SIZE);
        filtro.setSort("id");
        filtro.setDirection("ASC");

        final ListaPaginada<MovimentacaoItem> itens = movimentacaoItemDataProvider.buscarPorMovimentacaoId(filtro);

        this.page = this.page + 1;

        return itens.getItems();
    }

    private Long calcTotalPages() {
        final Long count = this.movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(this.movimentacaoId);
        return ((Double) Math.ceil((double) count / (double) CHUNK_SIZE)).longValue();
    }
}
