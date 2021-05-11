package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.arquivo;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.download.DownloadUsecase;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.download.DownloadUsecaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.download.converter.DownloadOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class DownloadArquivoFactory {

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean
    @DependsOn({"DownloadOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DownloadUsecase createDownloadUsecase(DownloadOutputDataConverter converter) {
        return new DownloadUsecaseImpl(sistemaDeArquivosIntegration, converter);
    }

    @Bean("DownloadOutputDataConverter")
    public DownloadOutputDataConverter createOutputDataConverter() {
        return new DownloadOutputDataConverter();
    }
}
