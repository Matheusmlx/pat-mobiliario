package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.converter.InserirReservaPorOrgaoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class InserirReservaPorOrgaoFactory {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    @Bean("InserirReservaPorOrgaoUseCase")
    @DependsOn({"InserirReservaPorOrgaoOutputDataConverter", "Clock"})
    public InserirReservaPorOrgaoUseCase createUseCase(InserirReservaPorOrgaoOutputDataConverter outputDataConverter,
                                                       Clock clock) {
        return new InserirReservaPorOrgaoUseCaseImpl(
            clock,
            reservaDataProvider,
            reservaIntervaloDataProvider,
            reservaIntervaloNumeroDataProvider,
            patrimonioDataProvider,
            outputDataConverter
        );
    }

    @Bean("InserirReservaPorOrgaoOutputDataConverter")
    public InserirReservaPorOrgaoOutputDataConverter createOutputDataConverter() {
        return new InserirReservaPorOrgaoOutputDataConverter();
    }

}
