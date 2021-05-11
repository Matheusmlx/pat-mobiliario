package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ResponsavelDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.converter.BuscarResponsaveisOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarResponsaveisFactory {

    @Autowired
    private ResponsavelDataProvider responsavelDataProvider;

    @Bean("BuscarResponsaveisUseCase")
    @DependsOn({"BuscarResponsaveisOutputDataConverter"})
    public BuscarResponsaveisUseCase createUseCase(BuscarResponsaveisOutputDataConverter outputDataConverter) {
        return new BuscarResponsaveisUseCaseImpl(
            responsavelDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarResponsaveisOutputDataConverter")
    public BuscarResponsaveisOutputDataConverter createOutputDataConverter() {
        return new BuscarResponsaveisOutputDataConverter();
    }

}
