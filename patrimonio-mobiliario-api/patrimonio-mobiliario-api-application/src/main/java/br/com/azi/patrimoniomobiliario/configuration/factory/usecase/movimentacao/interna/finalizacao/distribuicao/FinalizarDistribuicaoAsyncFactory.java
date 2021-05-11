package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.finalizacao.distribuicao;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.FinalizarDistribuicaoAsyncUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.FinalizarDistribuicaoAsyncUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.DistribuirPatrimonioHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class FinalizarDistribuicaoAsyncFactory {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final DistribuirPatrimonioHelper distribuirPatrimonioHelper;

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    private final Clock clock;

    @Bean("FinalizarDistribuicaoAsyncUseCase")
    public FinalizarDistribuicaoAsyncUseCase createUseCase() {
        return new FinalizarDistribuicaoAsyncUseCaseImpl(
            clock,
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            contaContabilDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            distribuirPatrimonioHelper,
            patrimonioMobiliarioProperties.getPatrimonioParaContaAlmoxarifado(),
            patrimonioMobiliarioProperties.getCodigoContaContabilAlmoxarifado()
        );
    }
}
