package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CadastrarPatrimonioFactory {

    @Autowired
    private PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("CadastrarPatrimonioUseCase")
    public CadastrarPatrimonioUseCase createUseCase(){
        return new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            patrimonioMobiliarioProperties.getQuantidadeDigitosNumeroPatrimonio()
        );
    }

}
