package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.EditarPatrimonioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.EditarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.converter.EditarPatrimonioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarPatrimonioFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Bean("EditarPatrimonioUseCase")
    @DependsOn({"EditarPatrimonioOutputDataConverter"})
    public EditarPatrimonioUseCase createUseCase(EditarPatrimonioOutputDataConverter outputDataConverter) {
        return new EditarPatrimonioUseCaseImpl(patrimonioDataProvider, outputDataConverter, sistemaDeArquivosIntegration, itemIncorporacaoDataProvider);
    }

    @Bean("EditarPatrimonioOutputDataConverter")
    public EditarPatrimonioOutputDataConverter createOutputDataConverter() {
        return new EditarPatrimonioOutputDataConverter();
    }
}
