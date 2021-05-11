package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.depreciacao;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.converter.CalcularDepreciacaoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class CalcularDepreciacaoFactory {

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final DepreciacaoDataProvider depreciacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Bean("CalcularDepreciacaoUseCase")
    @DependsOn({"CalcularDepreciacaoOutputDataConverter"})
    public CalcularDepreciacaoUseCase createUseCase(CalcularDepreciacaoOutputDataConverter outputDataConverter, Clock clock) {
        return new CalcularDepreciacaoUseCaseImpl(
            clock,
            patrimonioDataProvider,
            depreciacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioMobiliarioProperties.getDepreciaEmAlmoxarifado(),
            outputDataConverter
        );
    }

    @Bean("CalcularDepreciacaoOutputDataConverter")
    public CalcularDepreciacaoOutputDataConverter createOutputDataConverter() {
        return new CalcularDepreciacaoOutputDataConverter();
    }
}
