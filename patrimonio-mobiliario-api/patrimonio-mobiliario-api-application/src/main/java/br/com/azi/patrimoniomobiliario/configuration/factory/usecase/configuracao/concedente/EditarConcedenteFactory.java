package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.EditarConcedenteUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.EditarConcedenteUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.converter.EditarConcedenteOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarConcedenteFactory {

    @Autowired
    private ConcedenteDataProvider concedenteDataProvider;

    @Bean("EditarConcedenteUseCase")
    @DependsOn({"EditarConcedenteOutputDataConverter"})
    public EditarConcedenteUseCase useCase(EditarConcedenteOutputDataConverter outputDataConverter) {
        return new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            outputDataConverter
        );
    }

    @Bean("EditarConcedenteOutputDataConverter")
    public EditarConcedenteOutputDataConverter createConverter() {
        return new EditarConcedenteOutputDataConverter();
    }
}
