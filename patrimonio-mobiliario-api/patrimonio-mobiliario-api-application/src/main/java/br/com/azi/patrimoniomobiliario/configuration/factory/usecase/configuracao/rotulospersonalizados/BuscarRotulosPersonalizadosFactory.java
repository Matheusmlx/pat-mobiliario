package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.rotulospersonalizados;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados.converter.RotulosPersonalizadosConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarRotulosPersonalizadosFactory {

    @Autowired
    public PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Bean("BuscarRotulosPersonalizadosUseCase")
    @DependsOn("RotulosPersonalizadosConverter")
    public BuscarRotulosPersonalizadosUseCase createUseCase(RotulosPersonalizadosConverter outputDataConverter) {
        return new BuscarRotulosPersonalizadosUseCaseImpl(
            patrimonioMobiliarioProperties.getRotulosPersonalizados(),
            outputDataConverter
        );
    }

    @Bean("RotulosPersonalizadosConverter")
    public RotulosPersonalizadosConverter createConverter() {
        return new RotulosPersonalizadosConverter();
    }
}
