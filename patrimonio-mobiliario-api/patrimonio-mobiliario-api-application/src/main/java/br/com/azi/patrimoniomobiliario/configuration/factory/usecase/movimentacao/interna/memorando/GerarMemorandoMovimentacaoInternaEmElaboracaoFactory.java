package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.memorando;

import br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando.MemorandoMovimentacaoInternaUtils;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.GerarMemorandoMovimentacaoInternaEmElaboracaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.converter.GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class GerarMemorandoMovimentacaoInternaEmElaboracaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Autowired
    private MemorandoMovimentacaoInternaUtils memorandoMovimentacaoInternaUtils;

    @Bean("GerarMemorandoMovimentacaoInternaEmElaboracaoUseCase")
    @DependsOn("GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter")
    public GerarMemorandoMovimentacaoInternaEmElaboracaoUseCaseImpl createUseCase(GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter outputDataConverter){
        return new GerarMemorandoMovimentacaoInternaEmElaboracaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            sistemaDeRelatoriosIntegration,
            memorandoMovimentacaoInternaUtils,
            outputDataConverter);
    }

    @Bean("GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter")
    public GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter createOutputDataConverter(){
        return new GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter();
    }
}
