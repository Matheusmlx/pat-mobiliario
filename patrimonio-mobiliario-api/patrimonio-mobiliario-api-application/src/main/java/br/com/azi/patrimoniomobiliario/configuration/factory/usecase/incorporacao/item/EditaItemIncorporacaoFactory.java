package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NaturezaDespesaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.converter.EditaItemIncorporacaoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@RequiredArgsConstructor
public class EditaItemIncorporacaoFactory {

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final ConfigContaContabilDataProvider configContaContabilDataProvider;

    private final NaturezaDespesaDataProvider naturezaDespesaDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("EditaItemIncorporacaoUseCase")
    @DependsOn({"EditaItemIncorporacaoOutputDataConverter"})
    public EditaItemIncorporacaoUseCase createUseCase(EditaItemIncorporacaoOutputDataConverter outputDataConverter) {
        return new EditaItemIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            incorporacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            naturezaDespesaDataProvider,
            sistemaDeArquivosIntegration,
            outputDataConverter);
    }

    @Bean("EditaItemIncorporacaoOutputDataConverter")
    public EditaItemIncorporacaoOutputDataConverter createConverter() {
        return new EditaItemIncorporacaoOutputDataConverter();
    }
}
