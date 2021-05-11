package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.CadastrarReconhecimentoUsecase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.CadastrarReconhecimentoUsecaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.converter.CadastrarReconhecimentoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarReconhecimentoFactory {

    @Autowired
    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    @Bean("CadastrarReconhecimentoUsecase")
    @DependsOn({"CadastrarReconhecimentoOutputDataConverter"})
    public CadastrarReconhecimentoUsecase createUsecase(CadastrarReconhecimentoOutputDataConverter outputDataConverter) {
        return new CadastrarReconhecimentoUsecaseImpl(reconhecimentoDataProvider, outputDataConverter);
    }

    @Bean("CadastrarReconhecimentoOutputDataConverter")
    public CadastrarReconhecimentoOutputDataConverter createConverter() {
        return new CadastrarReconhecimentoOutputDataConverter();
    }
}
