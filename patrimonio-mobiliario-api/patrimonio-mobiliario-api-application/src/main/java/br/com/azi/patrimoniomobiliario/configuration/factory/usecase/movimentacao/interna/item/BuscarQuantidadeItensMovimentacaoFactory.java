package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.converter.BuscarQuantidadeItensMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarQuantidadeItensMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Bean("BuscarQuantidadeItensMovimentacaoUseCase")
    @DependsOn({"BuscarQuantidadeItensMovimentacaoOutputDataConverter"})
    public BuscarQuantidadeItensMovimentacaoUseCase createUseCase(BuscarQuantidadeItensMovimentacaoOutputDataConverter outputDataConverter) {
        return new BuscarQuantidadeItensMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarQuantidadeItensMovimentacaoOutputDataConverter")
    public BuscarQuantidadeItensMovimentacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarQuantidadeItensMovimentacaoOutputDataConverter();
    }
}
