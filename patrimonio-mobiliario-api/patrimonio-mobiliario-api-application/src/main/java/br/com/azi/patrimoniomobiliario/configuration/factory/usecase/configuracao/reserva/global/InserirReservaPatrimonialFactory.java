package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.converter.InserirReservaPatrimonialOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class InserirReservaPatrimonialFactory {

    private final Clock clock;

    private final ReservaDataProvider reservaDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    @Bean("InserirReservaPatrimonialUseCase")
    public InserirReservaPatrimonialUseCase createUseCase(InserirReservaPatrimonialOutputDataConverter outputDataConverter) {
        return new InserirReservaPatrimonialUseCaseImpl(
            clock,
            reservaDataProvider,
            patrimonioDataProvider,
            outputDataConverter
        );
    }

    @Bean("InserirReservaPatrimonialOutputDataConverter")
    public InserirReservaPatrimonialOutputDataConverter createOutputDataConverter() {
        return new InserirReservaPatrimonialOutputDataConverter();
    }

}
