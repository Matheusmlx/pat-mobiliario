package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.empenho;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.EditarEmpenhoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.EditarEmpenhoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.converter.EditarEmpenhoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@RequiredArgsConstructor
public class EditarEmpenhoFactory {

    private final EmpenhoDataProvider empenhoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    @Bean("EditarEmpenhoUseCase")
    @DependsOn({"EditarEmpenhoOutputDataConverter"})
    public EditarEmpenhoUseCase createUseCase(EditarEmpenhoOutputDataConverter outputDataConverter) {
        return new EditarEmpenhoUseCaseImpl(
            empenhoDataProvider,
            incorporacaoDataProvider,
            outputDataConverter
        );
    }

    @Bean("EditarEmpenhoOutputDataConverter")
    public EditarEmpenhoOutputDataConverter createOutputDataConverter() {
        return new EditarEmpenhoOutputDataConverter();
    }
}
