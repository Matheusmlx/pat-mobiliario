package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.BuscarOrgaoPorFiltroUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.BuscarOrgaoPorFiltroUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.converter.BuscarOrgaoPorFiltroOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BuscarOrgaoPorFiltroFactory {

    private final SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarOrgaoPorFiltroUseCase")
    public BuscarOrgaoPorFiltroUseCase createUseCase(BuscarOrgaoPorFiltroOutputDataConverter outputDataConverter) {
        return new BuscarOrgaoPorFiltroUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter
        );
    }

    @Bean("BuscarOrgaoPorFiltroOutputDataConverter")
    public BuscarOrgaoPorFiltroOutputDataConverter createConverter() {
        return new BuscarOrgaoPorFiltroOutputDataConverter();
    }
}
