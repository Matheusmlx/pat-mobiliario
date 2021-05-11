package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.BuscarIncorporacoesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.BuscarIncorporacoesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter.BuscarIncorporacoesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter.BuscarIncorporacoesOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarIncorporacoesFactory {

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("BuscarIncorporacoesUseCase")
    @DependsOn({"BuscarIncorporacoesFiltroConverter", "BuscarIncorporacoesOutputDataConverter"})
    public BuscarIncorporacoesUseCase createUseCase(BuscarIncorporacoesFiltroConverter filtroConverter, BuscarIncorporacoesOutputDataConverter outputDataConverter) {
        return new BuscarIncorporacoesUseCaseImpl(
            incorporacaoDataProvider,
            sessaoUsuarioDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean("BuscarIncorporacoesFiltroConverter")
    public BuscarIncorporacoesFiltroConverter createFiltroConverter() {
        return new BuscarIncorporacoesFiltroConverter();
    }

    @Bean("BuscarIncorporacoesOutputDataConverter")
    public BuscarIncorporacoesOutputDataConverter createConverter() {
        return new BuscarIncorporacoesOutputDataConverter();
    }
}
