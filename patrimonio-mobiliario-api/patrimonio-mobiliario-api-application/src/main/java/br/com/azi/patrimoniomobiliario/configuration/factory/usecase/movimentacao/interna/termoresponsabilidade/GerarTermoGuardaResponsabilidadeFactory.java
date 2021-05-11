package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.termoresponsabilidade;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.converter.GerarTermoGuardaResponsabilidadeOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class GerarTermoGuardaResponsabilidadeFactory {

    @Autowired
    private  MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private  MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Autowired
    private  SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;


    @Bean("GerarTermoGuardaResponsabilidadeUseCase")
    @DependsOn("GerarTermoGuardaResponsabilidadeOutputDataConverter")
    public GerarTermoGuardaResponsabilidadeUseCaseImpl createUseCase(GerarTermoGuardaResponsabilidadeOutputDataConverter outputDataConverter){
        return new GerarTermoGuardaResponsabilidadeUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            lancamentoContabilDataProvider,
            sistemaDeRelatoriosIntegration,
            outputDataConverter);
    }

    @Bean("GerarTermoGuardaResponsabilidadeOutputDataConverter")
    public GerarTermoGuardaResponsabilidadeOutputDataConverter createOutputDataConverter(){
        return new GerarTermoGuardaResponsabilidadeOutputDataConverter();
    }
}
