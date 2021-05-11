package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.BuscarMovimentacaoPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.BuscarMovimentacaoPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.converter.BuscarMovimentacaoPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarDistribuicaoPorIdFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("BuscarDistribuicaoPorIdUseCase")
    @DependsOn({"BuscarDistribuicaoPorIdOutputDataConverter"})
    public BuscarMovimentacaoPorIdUseCase createUseCase(BuscarMovimentacaoPorIdOutputDataConverter outputDataConverter) {
        return new BuscarMovimentacaoPorIdUseCaseImpl(movimentacaoDataProvider, outputDataConverter);
    }

    @Bean("BuscarDistribuicaoPorIdOutputDataConverter")
    public BuscarMovimentacaoPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarMovimentacaoPorIdOutputDataConverter();
    }

}
