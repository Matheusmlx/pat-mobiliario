package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.TrocarTipoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.TrocarTipoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.converter.TrocarTipoMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class TrocarTipoMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("TrocarTipoMovimentacaoUseCase")
    @DependsOn({"TrocarTipoMovimentacaoOutputDataConverter"})
    public TrocarTipoMovimentacaoUseCase createUseCase(TrocarTipoMovimentacaoOutputDataConverter converter) {
        return new TrocarTipoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            documentoDataProvider,
            sessaoUsuarioDataProvider,
            notaLancamentoContabilDataProvider,
            converter
        );
    }

    @Bean("TrocarTipoMovimentacaoOutputDataConverter")
    public TrocarTipoMovimentacaoOutputDataConverter createConverter() {
        return new TrocarTipoMovimentacaoOutputDataConverter();
    }
}
