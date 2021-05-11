package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.converter.EditarConvenioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarConvenioFactory {
    @Autowired
    private ConvenioDataProvider convenioDataProvider;

    @Bean("EditarConvenioUseCase")
    @DependsOn({"EditarConvenioOutputDataConverter"})
    public EditarConvenioUseCase createUsecase(EditarConvenioOutputDataConverter outputDataConverter) {
        return new EditarConvenioUseCaseImpl(convenioDataProvider, outputDataConverter);
    }

    @Bean("EditarConvenioOutputDataConverter")
    public EditarConvenioOutputDataConverter createConverter() {
        return new EditarConvenioOutputDataConverter();
    }
}
