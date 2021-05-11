package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.EstornarPatrimonioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.EstornarPatrimonioUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class EstornarPatrimonioFactory {

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Bean("EstornarPatrimonioUseCase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EstornarPatrimonioUseCase createUseCase() {
        return new EstornarPatrimonioUseCaseImpl(incorporacaoDataProvider,itemIncorporacaoDataProvider, patrimonioDataProvider, lancamentoContabilDataProvider);
    }
}
