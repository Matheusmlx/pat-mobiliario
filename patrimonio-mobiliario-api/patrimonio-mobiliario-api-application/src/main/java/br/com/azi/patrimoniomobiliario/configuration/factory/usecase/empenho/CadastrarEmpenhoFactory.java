package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.empenho;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.CadastrarEmpenhoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.CadastrarEmpenhoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.converter.CadastrarEmpenhoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@RequiredArgsConstructor
public class CadastrarEmpenhoFactory {

    private final EmpenhoDataProvider empenhoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    @Bean("CadastrarEmpenhoUseCase")
    @DependsOn({"CadastrarEmpenhoOutputDataConverter"})
    public CadastrarEmpenhoUseCase createUseCase(CadastrarEmpenhoOutputDataConverter outputDataConverter) {
        return new CadastrarEmpenhoUseCaseImpl(
            empenhoDataProvider,
            incorporacaoDataProvider,
            outputDataConverter
        );
    }

    @Bean("CadastrarEmpenhoOutputDataConverter")
    public CadastrarEmpenhoOutputDataConverter createOutputDataConverter() {
        return new CadastrarEmpenhoOutputDataConverter();
    }
}
