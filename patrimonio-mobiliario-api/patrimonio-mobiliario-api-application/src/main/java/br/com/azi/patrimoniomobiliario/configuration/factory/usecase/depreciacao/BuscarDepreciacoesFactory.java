package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.depreciacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.BuscarDepreciacoesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.BuscarDepreciacoesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.converter.BuscarDepreciacoesOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@RequiredArgsConstructor
public class BuscarDepreciacoesFactory {

    private final DepreciacaoDataProvider depreciacaoDataProvider;

    @Bean("BuscarDepreciacoesUseCase")
    @DependsOn("BuscarDepreciacoesOutputDataConverter")
    public BuscarDepreciacoesUseCase createUseCase(BuscarDepreciacoesOutputDataConverter outputDataConverter) {
        return new BuscarDepreciacoesUseCaseImpl(depreciacaoDataProvider, outputDataConverter);
    }

    @Bean("BuscarDepreciacoesOutputDataConverter")
    public BuscarDepreciacoesOutputDataConverter createOutputDataConverter() {
        return new BuscarDepreciacoesOutputDataConverter();
    }
}
