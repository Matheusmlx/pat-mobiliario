package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.RemoverMovimentacaoInternaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.RemoverMovimentacaoInternaUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverMovimentacaoInternaUseCaseFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Autowired
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Bean("RemoverMovimentacaoInternaUseCase")
    public RemoverMovimentacaoInternaUseCase createUseCase() {
        return new RemoverMovimentacaoInternaUseCaseImpl(
            movimentacaoDataProvider,
            documentoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            lancamentoContabilDataProvider
        );
    }

}
