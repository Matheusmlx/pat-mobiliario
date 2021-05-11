package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.edicao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.converter.EditarMovimentacaoEntreEstoquesOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarMovimentacaoEntreEstoquesFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarMovimentacaoEntreEstoquesUseCase")
    @DependsOn({"EditarMovimentacaoEntreEstoquesOutputDataConverter"})
    public EditarMovimentacaoEntreEstoquesUseCase createUseCase(EditarMovimentacaoEntreEstoquesOutputDataConverter converter) {
        return new EditarMovimentacaoEntreEstoquesUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            converter
        );
    }

    @Bean("EditarMovimentacaoEntreEstoquesOutputDataConverter")
    public EditarMovimentacaoEntreEstoquesOutputDataConverter createConverter() {
        return new EditarMovimentacaoEntreEstoquesOutputDataConverter();
    }
}
