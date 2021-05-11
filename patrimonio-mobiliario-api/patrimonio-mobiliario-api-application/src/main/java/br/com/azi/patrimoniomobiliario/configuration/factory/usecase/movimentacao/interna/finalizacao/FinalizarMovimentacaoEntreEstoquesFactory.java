package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.finalizacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.converter.FinalizarMovimentacaoEntreEstoquesOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class FinalizarMovimentacaoEntreEstoquesFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("FinalizarMovimentacaoEntreEstoquesUseCase")
    @DependsOn({"FinalizarMovimentacaoEntreEstoquesOutputDataConverter", "Clock"})
    public FinalizarMovimentacaoEntreEstoquesUseCase createUseCase(FinalizarMovimentacaoEntreEstoquesOutputDataConverter outputDataConverter,
                                                                   Clock clock) {
        return new FinalizarMovimentacaoEntreEstoquesUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            outputDataConverter,
            clock);
    }

    @Bean("FinalizarMovimentacaoEntreEstoquesOutputDataConverter")
    public FinalizarMovimentacaoEntreEstoquesOutputDataConverter createOutputDataConverter() {
        return new FinalizarMovimentacaoEntreEstoquesOutputDataConverter();
    }

}
