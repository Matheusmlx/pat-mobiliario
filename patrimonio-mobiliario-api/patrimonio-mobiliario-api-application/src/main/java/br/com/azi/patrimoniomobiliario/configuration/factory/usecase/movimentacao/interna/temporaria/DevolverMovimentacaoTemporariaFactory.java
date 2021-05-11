package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.converter.DevolverMovimentacaoTemporariaOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class DevolverMovimentacaoTemporariaFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Bean("DevolverMovimentacaoTemporariaUseCase")
    @DependsOn({"DevolverMovimentacaoTemporariaOutputDataConverter", "Clock"})
    public DevolverMovimentacaoTemporariaUseCase createUseCase(DevolverMovimentacaoTemporariaOutputDataConverter outputDataConverter,
                                                               Clock clock) {
        return new DevolverMovimentacaoTemporariaUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            clock,
            outputDataConverter
        );
    }

    @Bean("DevolverMovimentacaoTemporariaOutputDataConverter")
    public DevolverMovimentacaoTemporariaOutputDataConverter createOutputDataConverter() {
        return new DevolverMovimentacaoTemporariaOutputDataConverter();
    }
}
