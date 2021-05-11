package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CadastrarReservaIntervaloFactory {

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("CadastrarReservaIntervaloUseCase")
    public CadastrarReservaIntervaloUseCase createUseCase() {
        return new CadastrarReservaIntervaloUseCaseImpl(
                reservaIntervaloDataProvider,
                reservaDataProvider,
                patrimonioDataProvider,
                sessaoUsuarioDataProvider
        );
    }
}
