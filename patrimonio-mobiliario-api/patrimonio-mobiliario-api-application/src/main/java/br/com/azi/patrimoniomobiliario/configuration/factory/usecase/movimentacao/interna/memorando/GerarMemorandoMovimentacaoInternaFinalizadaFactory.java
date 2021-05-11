package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.memorando;

import br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando.MemorandoMovimentacaoInternaUtils;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.GerarMemorandoMovimentacaoInternaFinalizadaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.converter.GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class GerarMemorandoMovimentacaoInternaFinalizadaFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Autowired
    private MemorandoMovimentacaoInternaUtils memorandoMovimentacaoInternaUtils;

    @Autowired
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Bean("GerarMemorandoMovimentacaoInternaFinalizadaUseCase")
    @DependsOn("GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter")
    public GerarMemorandoMovimentacaoInternaFinalizadaUseCaseImpl createUseCase(GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter outputDataConverter){
        return new GerarMemorandoMovimentacaoInternaFinalizadaUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            lancamentoContabilDataProvider,
            sistemaDeRelatoriosIntegration,
            memorandoMovimentacaoInternaUtils,
            outputDataConverter);
    }

    @Bean("GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter")
    public GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter createOutputDataConverter(){
        return new GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter();
    }
}
