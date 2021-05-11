package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.edicao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.edicao.EditarDevolucaoAlmoxarifadoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.edicao.EditarDevolucaoAlmoxarifadoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.edicao.converter.EditarDevolucaoAlmoxarifadoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarDevolucaoAlmoxarifadoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarDevolucaoAlmoxarifadoUseCase")
    @DependsOn({"EditarDevolucaoAlmoxarifadoOutputDataConverter"})
    public EditarDevolucaoAlmoxarifadoUseCase createUseCase(EditarDevolucaoAlmoxarifadoOutputDataConverter converter) {
        return new EditarDevolucaoAlmoxarifadoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            converter
        );
    }

    @Bean("EditarDevolucaoAlmoxarifadoOutputDataConverter")
    public EditarDevolucaoAlmoxarifadoOutputDataConverter createConverter() {
        return new EditarDevolucaoAlmoxarifadoOutputDataConverter();
    }
}
