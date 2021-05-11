package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.converter.EditarReservaPatrimonialOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarReservaPatrimonialFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("EditarReservaPatrimonialUseCase")
    @DependsOn({"EditarReservaPatrimonialOutputDataConverter"})
    public EditarReservaPatrimonialUseCase createUseCase(EditarReservaPatrimonialOutputDataConverter outputDataConverter) {
        return new EditarReservaPatrimonialUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            patrimonioDataProvider,
            sessaoUsuarioDataProvider,
            outputDataConverter
        );
    }

    @Bean("EditarReservaPatrimonialOutputDataConverter")
    public EditarReservaPatrimonialOutputDataConverter createOutputDataConverter() {
        return new EditarReservaPatrimonialOutputDataConverter();
    }

}
