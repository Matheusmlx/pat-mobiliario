package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.FinalizarIncorporacaoErroUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.FinalizarIncorporacaoErroUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class FinalizarIncorporacaoErroFactory {

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final Clock clock;

    @Bean("FinalizarIncorporacaoErroUseCase")
    public FinalizarIncorporacaoErroUseCase createUseCase() {
        return new FinalizarIncorporacaoErroUseCaseImpl(
            incorporacaoDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            clock
        );
    }
}
