package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.BuscarTipoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.BuscarTipoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.converter.BuscarTipoMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarTipoMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("BuscarTipoMovimentacaoUseCase")
    @DependsOn({"BuscarTipoMovimentacaoOutputDataConverter"})
    public BuscarTipoMovimentacaoUseCase createUseCase(BuscarTipoMovimentacaoOutputDataConverter outputDataConverter) {
        return new BuscarTipoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarTipoMovimentacaoOutputDataConverter")
    public BuscarTipoMovimentacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarTipoMovimentacaoOutputDataConverter();
    }

}
