package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.contacontabil;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.converter.EditarContaContabilOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarConfigContaContabilFactory {
    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Bean("EditarConfigContaContabilUseCase")
    @DependsOn({"EditarConfigContaContabilOutputDataConverter"})
    public EditarContaContabilUseCase createUseCase(EditarContaContabilOutputDataConverter outputDataConverter) {
        return new EditarContaContabilUseCaseImpl(configContaContabilDataProvider, outputDataConverter);
    }

    @Bean("EditarConfigContaContabilOutputDataConverter")
    public EditarContaContabilOutputDataConverter createConverter() {
        return new EditarContaContabilOutputDataConverter();
    }
}
