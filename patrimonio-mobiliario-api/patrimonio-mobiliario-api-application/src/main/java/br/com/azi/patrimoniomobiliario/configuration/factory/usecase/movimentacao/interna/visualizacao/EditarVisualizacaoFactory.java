package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.visualizacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.EditarVisualizacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.EditarVisualizacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.converter.EditarVisualizacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarVisualizacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarVisualizacaoUseCase")
    @DependsOn({"EditarVisualizacaoOutputDataConverter"})
    public EditarVisualizacaoUseCase createUseCase(EditarVisualizacaoOutputDataConverter converver) {
        return new EditarVisualizacaoUseCaseImpl(
            movimentacaoDataProvider,
            notaLancamentoContabilDataProvider,
            converver
        );
    }

    @Bean("EditarVisualizacaoOutputDataConverter")
    public EditarVisualizacaoOutputDataConverter createConverter() {
        return new EditarVisualizacaoOutputDataConverter();
    }
}
