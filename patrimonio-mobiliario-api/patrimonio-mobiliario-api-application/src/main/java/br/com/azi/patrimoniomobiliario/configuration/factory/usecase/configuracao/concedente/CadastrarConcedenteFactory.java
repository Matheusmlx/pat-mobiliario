package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.CadastrarConcedenteUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.CadastrarConcedenteUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.converter.CadastrarConcedenteOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarConcedenteFactory {

    @Autowired
    private ConcedenteDataProvider concedenteDataProvider;

    @Bean("CadastrarConcedenteUseCase")
    @DependsOn({"CadastrarConcedenteOutputDataConverter"})
    public CadastrarConcedenteUseCase useCase(CadastrarConcedenteOutputDataConverter outputDataConverter){
        return new CadastrarConcedenteUseCaseImpl(
            concedenteDataProvider,
            outputDataConverter
        );
    }

    @Bean("CadastrarConcedenteOutputDataConverter")
    public CadastrarConcedenteOutputDataConverter createConverter(){
        return new CadastrarConcedenteOutputDataConverter();
    }
}
