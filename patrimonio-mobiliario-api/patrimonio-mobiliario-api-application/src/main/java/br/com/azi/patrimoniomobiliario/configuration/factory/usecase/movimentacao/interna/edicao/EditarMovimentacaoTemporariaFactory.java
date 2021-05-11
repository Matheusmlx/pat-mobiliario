package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.edicao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.EditarMovimentacaoTemporariaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.EditarMovimentacaoTemporariaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.converter.EditarMovimentacaoTemporariaOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarMovimentacaoTemporariaFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarMovimentacaoTemporariaUseCase")
    @DependsOn({"EditarMovimentacaoTemporariaOutputDataConverter"})
    public EditarMovimentacaoTemporariaUseCase createUseCase(EditarMovimentacaoTemporariaOutputDataConverter converter) {
        return new EditarMovimentacaoTemporariaUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            converter
        );
    }

    @Bean("EditarMovimentacaoTemporariaOutputDataConverter")
    public EditarMovimentacaoTemporariaOutputDataConverter createConverter() {
        return new EditarMovimentacaoTemporariaOutputDataConverter();
    }
}
