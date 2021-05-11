package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers.IncorporarPatrimonioHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class IncorporarPatrimonioHelperFactory {

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Bean("IncorporarPatrimonioHelper")
    public IncorporarPatrimonioHelper createIncorporarPatrimonioHelper() {
        return new IncorporarPatrimonioHelper(
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            patrimonioMobiliarioProperties.getPatrimonioParaContaAlmoxarifado()
        );
    }
}
