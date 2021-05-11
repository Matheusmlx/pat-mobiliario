package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EstadoConservacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.converter.CadastrarItemIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarItemIncorporacaoFactory {

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private EstadoConservacaoDataProvider estadoConservacaoDataProvider;

    @Bean("CadastrarItemIncorporacaoUseCase")
    @DependsOn({"CadastrarItemIncorporacaoOutputDataConverter"})
    public CadastrarItemIncorporacaoUseCase useCase(CadastrarItemIncorporacaoOutputDataConverter outputDataConverter){
        return new CadastrarItemIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            incorporacaoDataProvider,
            estadoConservacaoDataProvider,
            outputDataConverter
        );
    }

    @Bean("CadastrarItemIncorporacaoOutputDataConverter")
    public CadastrarItemIncorporacaoOutputDataConverter createConverter(){
        return new CadastrarItemIncorporacaoOutputDataConverter();
    }
}
