package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.notificacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.converter.RemoverNotificacoesAntigasOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class RemoverNotificacoesAntigasFactory {

    private final Clock clock;

    private final NotificacaoDataProvider notificacaoDataProvider;

    @Bean("RemoverNotificacoesAntigasUseCase")
    @DependsOn("RemoverNotificacoesAntigasOutputDataConverter")
    public RemoverNotificacoesAntigasUseCase createUseCase(RemoverNotificacoesAntigasOutputDataConverter converter) {
        return new RemoverNotificacoesAntigasUseCaseImpl(
            clock,
            notificacaoDataProvider,
            converter
        );
    }

    @Bean("RemoverNotificacoesAntigasOutputDataConverter")
    public RemoverNotificacoesAntigasOutputDataConverter createConverter() {
        return new RemoverNotificacoesAntigasOutputDataConverter();
    }
}
