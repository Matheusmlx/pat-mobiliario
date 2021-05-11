package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.finalizacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.converter.FinalizarMovimentacaoEntreSetoresOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class FinalizarMovimentacaoEntreSetoresFactory {

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

    @Bean("FinalizarMovimentacaoDefinitivaUseCase")
    @DependsOn({"FinalizarMovimentacaoDefinitivaOutputDataConverter", "Clock"})
    public FinalizarMovimentacaoEntreSetoresUseCase createUseCase(FinalizarMovimentacaoEntreSetoresOutputDataConverter outputDataConverter,
                                                                  Clock clock) {
        return new FinalizarMovimentacaoEntreSetoresUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            clock,
            outputDataConverter
        );
    }

    @Bean("FinalizarMovimentacaoDefinitivaOutputDataConverter")
    public FinalizarMovimentacaoEntreSetoresOutputDataConverter createOutputDataConverter() {
        return new FinalizarMovimentacaoEntreSetoresOutputDataConverter();
    }
}
