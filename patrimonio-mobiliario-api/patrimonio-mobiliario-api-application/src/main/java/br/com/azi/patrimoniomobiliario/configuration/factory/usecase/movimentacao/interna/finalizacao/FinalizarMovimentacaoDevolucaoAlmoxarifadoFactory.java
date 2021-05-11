package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.finalizacao;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.converter.FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class FinalizarMovimentacaoDevolucaoAlmoxarifadoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private ContaContabilDataProvider contaContabilDataProvider;

    @Autowired
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Bean("FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase")
    @DependsOn({"FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter", "Clock"})
    public FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase createUseCase(FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter outputDataConverter,
                                                                           Clock clock) {
        return new FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            contaContabilDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            patrimonioMobiliarioProperties.getPatrimonioParaContaAlmoxarifado(),
            patrimonioMobiliarioProperties.getCodigoContaContabilAlmoxarifado(),
            clock,
            outputDataConverter
        );
    }

    @Bean("FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter")
    public FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter createOutputDataConverter() {
        return new FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter();
    }

}
