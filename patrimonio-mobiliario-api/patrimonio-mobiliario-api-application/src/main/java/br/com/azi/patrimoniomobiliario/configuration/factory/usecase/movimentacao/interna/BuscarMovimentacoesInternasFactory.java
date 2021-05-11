package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.BuscarMovimentacoesInternasUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.BuscarMovimentacoesInternasUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter.BuscarMovimentacoesInternasFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter.BuscarMovimentacoesInternasOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarMovimentacoesInternasFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean({"BuscarMovimentacoesInternasUseCase"})
    @DependsOn({"BuscarMovimentacoesInternasFiltroConverter", "BuscarMovimentacoesInternasOutputDataConverter"})
    public BuscarMovimentacoesInternasUseCase createUseCase(
        BuscarMovimentacoesInternasFiltroConverter filtroConverter,
        BuscarMovimentacoesInternasOutputDataConverter outputDataConverter) {
        return new BuscarMovimentacoesInternasUseCaseImpl(
            movimentacaoDataProvider,
            sessaoUsuarioDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean({"BuscarMovimentacoesInternasFiltroConverter"})
    public BuscarMovimentacoesInternasFiltroConverter createFiltroConverter() {
        return new BuscarMovimentacoesInternasFiltroConverter();
    }

    @Bean({"BuscarMovimentacoesInternasOutputDataConverter"})
    public BuscarMovimentacoesInternasOutputDataConverter createOutputDataConverter() {
        return new BuscarMovimentacoesInternasOutputDataConverter();
    }

}
