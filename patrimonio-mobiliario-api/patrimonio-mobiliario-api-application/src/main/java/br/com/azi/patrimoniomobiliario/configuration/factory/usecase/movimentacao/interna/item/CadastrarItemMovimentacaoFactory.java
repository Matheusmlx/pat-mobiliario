package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.converter.CadastrarItemMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarItemMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean(value = "CadastrarItemMovimentacaoUseCase")
    @DependsOn(value = {"CadastrarItemMovimentacaoOutputDataConverter"})
    public CadastrarItemMovimentacaoUseCase createUseCase(CadastrarItemMovimentacaoOutputDataConverter converter) {
        return new CadastrarItemMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            converter
        );
    }

    @Bean(value = "CadastrarItemMovimentacaoOutputDataConverter")
    public CadastrarItemMovimentacaoOutputDataConverter createConverter() {
        return new CadastrarItemMovimentacaoOutputDataConverter();
    }
}
