package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.converter.EnviarMovimentacaoTemporariaOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class EnviarMovimentacaoTemporariaFactory {

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

    @Bean("EnviarMovimentacaoTemporariaUseCase")
    @DependsOn({"EnviarMovimentacaoTemporariaOutputDataConverter", "Clock"})
    public EnviarMovimentacaoTemporariaUseCase createUseCase(EnviarMovimentacaoTemporariaOutputDataConverter outputDataConverter,
                                                             Clock clock) {
        return new EnviarMovimentacaoTemporariaUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            clock,
            outputDataConverter
        );
    }

    @Bean("EnviarMovimentacaoTemporariaOutputDataConverter")
    public EnviarMovimentacaoTemporariaOutputDataConverter createOutputDataConverter() {
        return new EnviarMovimentacaoTemporariaOutputDataConverter();
    }

}
