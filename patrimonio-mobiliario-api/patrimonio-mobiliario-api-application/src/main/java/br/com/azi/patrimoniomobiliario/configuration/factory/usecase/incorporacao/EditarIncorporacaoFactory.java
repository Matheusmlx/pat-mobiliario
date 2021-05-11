package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.converter.EditarIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarIncorporacaoFactory {

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarIncorporacaoUseCase")
    @DependsOn({"EditarIncorporacaoOutputDataConverter"})
    public EditarIncorporacaoUseCase createUseCase(EditarIncorporacaoOutputDataConverter outputDataConverter) {
        return new EditarIncorporacaoUseCaseImpl(incorporacaoDataProvider, notaLancamentoContabilDataProvider, outputDataConverter);
    }

    @Bean("EditarIncorporacaoOutputDataConverter")
    public EditarIncorporacaoOutputDataConverter createOutputDataConverter() {
        return new EditarIncorporacaoOutputDataConverter();
    }
}
