package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.RemoverItemMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.RemoverItemMovimentacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverItemMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Bean({"RemoverItemMovimentacaoUseCase"})
    public RemoverItemMovimentacaoUseCase createUseCase() {
        return new RemoverItemMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            patrimonioDataProvider,
            movimentacaoItemDataProvider
        );
    }

}
