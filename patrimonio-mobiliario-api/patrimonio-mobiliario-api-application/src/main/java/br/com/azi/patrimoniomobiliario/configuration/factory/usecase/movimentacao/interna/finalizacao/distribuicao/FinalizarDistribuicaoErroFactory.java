package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.finalizacao.distribuicao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.FinalizarDistribuicaoErroUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.FinalizarDistribuicaoErroUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class FinalizarDistribuicaoErroFactory {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final Clock clock;

    @Bean("FinalizarDistribuicaoErroUseCase")
    public FinalizarDistribuicaoErroUseCase createUseCase() {
        return new FinalizarDistribuicaoErroUseCaseImpl(
            movimentacaoDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            clock
        );
    }
}
