package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.converter.BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarInformacaoDevolucaoPatrimoniosFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Bean("BuscarInformacaoDevolucaoPatrimoniosUseCase")
    @DependsOn({"BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter"})
    public BuscarInformacaoDevolucaoPatrimoniosUseCase createUseCase(BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter outputDataConverter) {
        return new BuscarInformacaoDevolucaoPatrimoniosUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter")
    public BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter createOutputDataConverter() {
        return new BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter();
    }

}
