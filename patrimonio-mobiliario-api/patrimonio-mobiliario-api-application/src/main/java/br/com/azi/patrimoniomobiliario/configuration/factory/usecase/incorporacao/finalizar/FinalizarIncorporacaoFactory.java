package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.FinalizarIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.converter.FinalizarIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers.IncorporarPatrimonioHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class FinalizarIncorporacaoFactory {

    private final Clock clock;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final ConfigContaContabilDataProvider configContaContabilDataProvider;

    private final ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    private final IncorporarPatrimonioHelper incorporarPatrimonioHelper;

    @Bean("FinalizarIncorporacaoUseCase")
    @DependsOn({"FinalizarIncorporacaoOutputConverter"})
    public FinalizarIncorporacaoUseCaseImpl createUseCase(FinalizarIncorporacaoOutputDataConverter outputDataConverter) {
        return new FinalizarIncorporacaoUseCaseImpl(
            clock,
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            contaContabilDataProvider,
            sessaoUsuarioDataProvider,
            configContaContabilDataProvider,
                configuracaoDepreciacaoDataProvider,
            notificacaoDataProvider,
            sistemaDeArquivosIntegration,
            patrimonioMobiliarioProperties.getBloquearValoresDivergentesAoFinalizar(),
            patrimonioMobiliarioProperties.getPatrimonioParaContaAlmoxarifado(),
            patrimonioMobiliarioProperties.getCodigoContaContabilAlmoxarifado(),
            patrimonioMobiliarioProperties.getLimiteRegistrosProcessamentoSincrono(),
            incorporarPatrimonioHelper,
            outputDataConverter
        );
    }

    @Bean("FinalizarIncorporacaoOutputConverter")
    public FinalizarIncorporacaoOutputDataConverter createOutputDataConverter() {
        return new FinalizarIncorporacaoOutputDataConverter();
    }
}
