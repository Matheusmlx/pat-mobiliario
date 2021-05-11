package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.arquivo;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.UploadTemporarioUsecase;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.UploadTemporarioUsecaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.converter.UploadTemporarioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class UploadArquivoTemporarioFactory {

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean
    @DependsOn({"UploadTemporarioOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UploadTemporarioUsecase createUploadTemporarioUsecase(UploadTemporarioOutputDataConverter converter) {
        return new UploadTemporarioUsecaseImpl(sistemaDeArquivosIntegration, converter);
    }

    @Bean("UploadTemporarioOutputDataConverter")
    public UploadTemporarioOutputDataConverter createOutputDataConverter() {
        return new UploadTemporarioOutputDataConverter();
    }
}
