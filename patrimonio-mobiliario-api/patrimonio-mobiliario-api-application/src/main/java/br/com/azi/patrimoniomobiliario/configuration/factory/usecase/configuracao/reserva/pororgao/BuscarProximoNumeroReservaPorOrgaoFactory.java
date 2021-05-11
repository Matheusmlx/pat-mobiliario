package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.converter.BuscarProximoNumeroReservaPorOrgaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuscarProximoNumeroReservaPorOrgaoFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarProximoNumeroReservaPorOrgaoUseCase")
    public BuscarProximoNumeroReservaPorOrgaoUseCase createUseCase(BuscarProximoNumeroReservaPorOrgaoOutputDataConverter outputDataConverter) {
        return new BuscarProximoNumeroReservaPorOrgaoUseCaseImpl(
                reservaDataProvider,
                patrimonioDataProvider,
                outputDataConverter
        );
    }

    @Bean("BuscarProximoNumeroReservaPorOrgaoOutputDataConverter")
    public BuscarProximoNumeroReservaPorOrgaoOutputDataConverter createOutputDataConverter() {
        return new BuscarProximoNumeroReservaPorOrgaoOutputDataConverter();
    }

}
