package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter.BuscarItensMovimentacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter.BuscarItensMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarItensMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Bean(value = "BuscarItensMovimentacaoUseCase")
    @DependsOn(value = {"BuscarItensMovimentacaoFiltroConverter", "BuscarItensMovimentacaoOutputDataConverter"})
    public BuscarItensMovimentacaoUseCase createUseCase(BuscarItensMovimentacaoFiltroConverter filtroConverter, BuscarItensMovimentacaoOutputDataConverter outputDataConverter) {
        return new BuscarItensMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean(value = "BuscarItensMovimentacaoFiltroConverter")
    public BuscarItensMovimentacaoFiltroConverter createFiltroConverter() {
        return new BuscarItensMovimentacaoFiltroConverter();
    }

    @Bean(value = "BuscarItensMovimentacaoOutputDataConverter")
    public BuscarItensMovimentacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarItensMovimentacaoOutputDataConverter();
    }
}
