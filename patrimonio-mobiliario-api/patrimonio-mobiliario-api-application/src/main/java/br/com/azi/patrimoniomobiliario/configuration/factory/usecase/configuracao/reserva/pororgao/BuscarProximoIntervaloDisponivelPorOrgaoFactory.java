package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.converter.BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuscarProximoIntervaloDisponivelPorOrgaoFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarProximoIntervaloDisponivelPorOrgaoUseCase")
    public BuscarProximoIntervaloDisponivelPorOrgaoUseCase createUseCase(BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter outputDataConverter){
        return new BuscarProximoIntervaloDisponivelPorOrgaoUseCaseImpl(
            reservaDataProvider,
            patrimonioDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter")
    public BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter createOutputDataConverter() {
        return new BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter();
    }
}
