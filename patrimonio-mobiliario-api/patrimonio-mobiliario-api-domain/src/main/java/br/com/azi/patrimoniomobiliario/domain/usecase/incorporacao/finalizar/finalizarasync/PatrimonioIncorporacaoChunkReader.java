package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import lombok.Getter;

import java.util.List;

@Getter
public class PatrimonioIncorporacaoChunkReader {
    private static final Long CHUNK_SIZE = 10L;

    private Long page = 0L;
    private Long totalPages = 0L;

    private final PatrimonioDataProvider patrimonioDataProvider;
    private final Long incorporacaoId;

    public PatrimonioIncorporacaoChunkReader(final PatrimonioDataProvider patrimonioDataProvider, final Long incorporacaoId) {
        this.patrimonioDataProvider = patrimonioDataProvider;
        this.incorporacaoId = incorporacaoId;
        this.totalPages = this.calcTotalPages();
    }

    public boolean hasNext() {
        return this.page < this.totalPages;
    }

    public List<Patrimonio> next() {
        final Patrimonio.Filtro filtro = Patrimonio.Filtro.builder()
            .incorporacaoId(incorporacaoId)
            .build();

        filtro.setPage(this.page);
        filtro.setSize(CHUNK_SIZE);
        filtro.setSort("id");
        filtro.setDirection("ASC");

        final List<Patrimonio> patrimonios = patrimonioDataProvider.buscarPatrimoniosPorIncorporacao(filtro);

        this.page = this.page + 1;

        return patrimonios;
    }

    private Long calcTotalPages() {
        final Long count = this.patrimonioDataProvider.buscarQuantidadePatrimoniosPorIncorporacaoId(this.incorporacaoId);
        return ((Double) Math.ceil((double) count / (double) CHUNK_SIZE)).longValue();
    }
}
