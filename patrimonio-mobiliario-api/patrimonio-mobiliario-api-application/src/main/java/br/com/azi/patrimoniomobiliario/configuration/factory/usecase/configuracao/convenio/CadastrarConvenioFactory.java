package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.converter.CadastrarConvenioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarConvenioFactory {

    @Autowired
    private ConvenioDataProvider convenioDataProvider;

    @Bean("CadastrarConvenioUseCase")
    @DependsOn({"CadastrarConvenioOutputDataConverter"})
    public CadastrarConvenioUseCase createUseCase(CadastrarConvenioOutputDataConverter outputDataConverter) {
        return new CadastrarConvenioUseCaseImpl(
            convenioDataProvider,
            outputDataConverter);
    }

    @Bean("CadastrarConvenioOutputDataConverter")
    public CadastrarConvenioOutputDataConverter createConverter() {
        return new CadastrarConvenioOutputDataConverter();
    }
}
