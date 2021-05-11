package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.FinalizarIncorporacaoAsyncUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.FinalizarIncorporacaoAsyncUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers.IncorporarPatrimonioHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class FinalizarIncorporacaoAsyncFactory {

    private final Clock clock;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    private final IncorporarPatrimonioHelper incorporarPatrimonioHelper;

    @Bean("FinalizarIncorporacaoAsyncUseCase")
    public FinalizarIncorporacaoAsyncUseCase createUseCase() {
        return new FinalizarIncorporacaoAsyncUseCaseImpl(
            clock,
            patrimonioDataProvider,
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            contaContabilDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            incorporarPatrimonioHelper,
            patrimonioMobiliarioProperties.getPatrimonioParaContaAlmoxarifado(),
            patrimonioMobiliarioProperties.getCodigoContaContabilAlmoxarifado()
        );
    }
}
