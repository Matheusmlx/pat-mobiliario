package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.edicao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.EditarDistribuicaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.EditarDistribuicaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.converter.EditarDistribuicaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarDistribuicaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarDistribuicaoUseCase")
    @DependsOn({"EditarDistribuicaoOutputDataConverter"})
    public EditarDistribuicaoUseCase createUseCase(EditarDistribuicaoOutputDataConverter converter) {
        return new EditarDistribuicaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            converter
        );
    }

    @Bean("EditarDistribuicaoOutputDataConverter")
    public EditarDistribuicaoOutputDataConverter createConverter() {
        return new EditarDistribuicaoOutputDataConverter();
    }
}
