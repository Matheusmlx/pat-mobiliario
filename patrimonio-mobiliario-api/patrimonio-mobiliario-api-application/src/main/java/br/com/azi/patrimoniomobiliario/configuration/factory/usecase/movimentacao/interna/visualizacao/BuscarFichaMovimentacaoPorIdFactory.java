package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.visualizacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.BuscarFichaMovimentacaoPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.BuscarFichaMovimentacaoPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.converter.BuscarFichaMovimentacaoPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarFichaMovimentacaoPorIdFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("BuscarFichaMovimentacaoPorIdUseCase")
    @DependsOn({"BuscarFichaMovimentacaoPorIdOutputDataConverter"})
    public BuscarFichaMovimentacaoPorIdUseCase createUseCase(BuscarFichaMovimentacaoPorIdOutputDataConverter outputDataConverter) {
        return new BuscarFichaMovimentacaoPorIdUseCaseImpl(movimentacaoDataProvider, outputDataConverter);
    }

    @Bean("BuscarFichaMovimentacaoPorIdOutputDataConverter")
    public BuscarFichaMovimentacaoPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarFichaMovimentacaoPorIdOutputDataConverter();
    }
}
