package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.buscarporpatrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio.converter.BuscarMovimentacoesPorPatrimonioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarMovimentacoesPorPatrimonioUseCaseFactory {

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Bean("BuscarMovimentacoesPorPatrimonioUseCase")
    @DependsOn({"BuscarMovimentacoesPorPatrimonioOutputDataConverter"})
    public BuscarMovimentacoesPorPatrimonioUseCase createUseCase(BuscarMovimentacoesPorPatrimonioOutputDataConverter outputDataConverter) {
        return new BuscarMovimentacoesPorPatrimonioUseCaseImpl(
            movimentacaoItemDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarMovimentacoesPorPatrimonioOutputDataConverter")
    public BuscarMovimentacoesPorPatrimonioOutputDataConverter createOutputDataConverter() {
        return new BuscarMovimentacoesPorPatrimonioOutputDataConverter();
    }

}
