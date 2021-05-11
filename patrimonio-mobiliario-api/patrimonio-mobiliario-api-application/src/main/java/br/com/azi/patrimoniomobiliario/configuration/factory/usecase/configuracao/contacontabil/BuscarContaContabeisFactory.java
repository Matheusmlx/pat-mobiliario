package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.contacontabil;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.conveter.BuscarContasContabeisFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.conveter.BuscarContasContabeisOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarContaContabeisFactory {

    @Autowired
    private ContaContabilDataProvider contaContabilDataProvider;

    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Bean("BuscarContasContabeisUseCase")
    @DependsOn({"BuscarContasContabeisFiltroConverter","BuscarContasContabeisOutputDataConverter"})
    public BuscarContasContabeisUseCase createUseCase(BuscarContasContabeisFiltroConverter filtroConverter, BuscarContasContabeisOutputDataConverter outputDataConverter){
        return new BuscarContasContabeisUseCaseImpl(
            contaContabilDataProvider,
            configContaContabilDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean("BuscarContasContabeisFiltroConverter")
    public BuscarContasContabeisFiltroConverter createFiltroConverter(){
        return new BuscarContasContabeisFiltroConverter();
    }

    @Bean("BuscarContasContabeisOutputDataConverter")
    public BuscarContasContabeisOutputDataConverter createConverter(){
        return new BuscarContasContabeisOutputDataConverter();
    }
}
