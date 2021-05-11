package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.ExcluirItemIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.ExcluirItemIncorporacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ExcluirItemIncorporacaoFactory {

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Bean("ExcluirItemIncorporacaoUseCase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ExcluirItemIncorporacaoUseCase createUseCase() {
        return new ExcluirItemIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            incorporacaoDataProvider
        );
    }
}
