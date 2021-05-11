package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.parametros;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.entity.ParametrosSistema;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar.BuscarParametrosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar.BuscarParametrosUsecase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar.converter.BuscarParametrosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarParametrosFactory {

    @Autowired
    public PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Bean("BuscarParametrosUsecase")
    @DependsOn("BuscarParametrosOutputDataConverter")
    public BuscarParametrosUsecase createUseCase(BuscarParametrosOutputDataConverter outputDataConverter) {
        return new BuscarParametrosUseCaseImpl(
                ParametrosSistema.builder()
                        .urlChatSuporte(patrimonioMobiliarioProperties.getUrlChatSuporte())
                        .quantidadeDigitosNumeroPatrimonio(patrimonioMobiliarioProperties.getQuantidadeDigitosNumeroPatrimonio())
                        .reservaPatrimonialGlobal(patrimonioMobiliarioProperties.getReservaPatrimonialGlobal())
                        .build(),
                outputDataConverter);
    }

    @Bean("BuscarParametrosOutputDataConverter")
    public BuscarParametrosOutputDataConverter createConverter() {
        return new BuscarParametrosOutputDataConverter();
    }
}
