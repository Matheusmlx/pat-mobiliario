package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.BuscarSituacaoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.BuscarSituacaoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.converter.BuscarSitucacaoMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarSituacaoMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("BuscarSituacaoMovimentacaoUseCase")
    @DependsOn({"BuscarSituacaoMovimentacaoOutputDataConverter"})
    public BuscarSituacaoMovimentacaoUseCase createUseCase(BuscarSitucacaoMovimentacaoOutputDataConverter outputDataConverter) {
        return new BuscarSituacaoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarSituacaoMovimentacaoOutputDataConverter")
    public BuscarSitucacaoMovimentacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarSitucacaoMovimentacaoOutputDataConverter();
    }
}
