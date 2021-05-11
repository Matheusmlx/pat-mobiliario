package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.edicao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.EditarMovimentacaoEntreSetoresUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.EditarMovimentacaoEntreSetoresUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.converter.EditarMovimentacaoEntreSetoresOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarMovimentacaoDefinitivaFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarMovimentacaoDefinitivaUseCase")
    @DependsOn({"EditarMovimentacaoDefinitivaOutputDataConverter"})
    public EditarMovimentacaoEntreSetoresUseCase createUseCase(EditarMovimentacaoEntreSetoresOutputDataConverter converter) {
        return new EditarMovimentacaoEntreSetoresUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            converter
        );
    }

    @Bean("EditarMovimentacaoDefinitivaOutputDataConverter")
    public EditarMovimentacaoEntreSetoresOutputDataConverter createConverter() {
        return new EditarMovimentacaoEntreSetoresOutputDataConverter();
    }
}
