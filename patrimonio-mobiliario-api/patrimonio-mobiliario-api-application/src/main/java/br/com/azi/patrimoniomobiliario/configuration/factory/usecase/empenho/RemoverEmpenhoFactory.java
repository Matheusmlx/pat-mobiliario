package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.empenho;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RemoverEmpenhoFactory {

    private final EmpenhoDataProvider empenhoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    @Bean("RemoverEmpenhoUseCase")
    public RemoverEmpenhoUseCase createUseCase() {
        return new RemoverEmpenhoUseCaseImpl(
            empenhoDataProvider,
            incorporacaoDataProvider
        );
    }
}
