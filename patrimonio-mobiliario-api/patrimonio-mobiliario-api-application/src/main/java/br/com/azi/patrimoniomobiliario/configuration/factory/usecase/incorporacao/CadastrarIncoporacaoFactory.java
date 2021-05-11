package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro.CadastrarIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro.CadastrarIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro.converter.CadastrarIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarIncoporacaoFactory {

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Bean("CadastrarIncorporacaoUseCase")
    @DependsOn({"CadastrarIncorporacaoOutputDataConverter"})
    public CadastrarIncorporacaoUseCase createUsecase(CadastrarIncorporacaoOutputDataConverter outputDataConverter) {
        return new CadastrarIncorporacaoUseCaseImpl(incorporacaoDataProvider, outputDataConverter);
    }

    @Bean("CadastrarIncorporacaoOutputDataConverter")
    public CadastrarIncorporacaoOutputDataConverter createConverter() {
        return new CadastrarIncorporacaoOutputDataConverter();
    }
}
