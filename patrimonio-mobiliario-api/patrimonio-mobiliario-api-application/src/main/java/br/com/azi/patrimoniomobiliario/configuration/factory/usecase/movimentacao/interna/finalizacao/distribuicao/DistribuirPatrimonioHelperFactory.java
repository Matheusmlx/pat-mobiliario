package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.finalizacao.distribuicao;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.DistribuirPatrimonioHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class DistribuirPatrimonioHelperFactory {

    private final Clock clock;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    @Bean("DistribuirPatrimonioHelper")
    public DistribuirPatrimonioHelper createHelper() {
        return new DistribuirPatrimonioHelper(
            clock,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            movimentacaoItemDataProvider,
            configuracaoDepreciacaoDataProvider,
            patrimonioMobiliarioProperties.getPatrimonioParaContaAlmoxarifado()
        );
    }
}
