package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.depreciacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.persistirdepreciacao.PersistirDepreciacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.persistirdepreciacao.PersistirDepreciacaoUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PersistirDepreciacaoFactory {

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final DepreciacaoDataProvider depreciacaoDataProvider;

    @Bean("PersistirDepreciacaoUseCase")
    public PersistirDepreciacaoUseCase createUseCase() {
        return new PersistirDepreciacaoUseCaseImpl(patrimonioDataProvider, depreciacaoDataProvider);
    }
}
