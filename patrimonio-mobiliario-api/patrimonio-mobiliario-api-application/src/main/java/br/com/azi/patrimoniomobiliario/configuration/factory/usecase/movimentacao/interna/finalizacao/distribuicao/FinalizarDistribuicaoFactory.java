package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.finalizacao.distribuicao;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.FinalizarDistribuicaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.FinalizarDistribuicaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.converter.FinalizarDistribuicaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.DistribuirPatrimonioHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class FinalizarDistribuicaoFactory {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final DistribuirPatrimonioHelper distribuirPatrimonioHelper;

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Bean("FinalizarDistribuicaoUseCase")
    @DependsOn({"FinalizarDistribuicaoOutputDataConverter", "Clock"})
    public FinalizarDistribuicaoUseCase createUseCase(FinalizarDistribuicaoOutputDataConverter outputDataConverter, Clock clock) {
        return new FinalizarDistribuicaoUseCaseImpl(
            clock,
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            contaContabilDataProvider,
            sessaoUsuarioDataProvider,
            notificacaoDataProvider,
            patrimonioMobiliarioProperties.getPatrimonioParaContaAlmoxarifado(),
            patrimonioMobiliarioProperties.getCodigoContaContabilAlmoxarifado(),
            patrimonioMobiliarioProperties.getLimiteRegistrosProcessamentoSincrono(),
            distribuirPatrimonioHelper,
            outputDataConverter
        );
    }

    @Bean("FinalizarDistribuicaoOutputDataConverter")
    public FinalizarDistribuicaoOutputDataConverter createOutputDataConverter() {
        return new FinalizarDistribuicaoOutputDataConverter();
    }
}
