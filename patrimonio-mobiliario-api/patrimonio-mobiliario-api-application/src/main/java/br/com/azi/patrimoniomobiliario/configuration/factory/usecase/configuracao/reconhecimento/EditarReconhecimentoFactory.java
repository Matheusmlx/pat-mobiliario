package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.EditarReconhecimentoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.EditarReconhecimentoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.converter.EditarReconhecimentoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarReconhecimentoFactory {

    @Autowired
    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    @Bean("EditarReconhecimentoUseCase")
    @DependsOn({"EditarReconhecimentoOutputDataConverter"})
    public EditarReconhecimentoUseCase createUsecase(EditarReconhecimentoOutputDataConverter outputDataConverter) {
        return new EditarReconhecimentoUseCaseImpl(reconhecimentoDataProvider, outputDataConverter);
    }

    @Bean("EditarReconhecimentoOutputDataConverter")
    public EditarReconhecimentoOutputDataConverter createConverter() {
        return new EditarReconhecimentoOutputDataConverter();
    }
}
