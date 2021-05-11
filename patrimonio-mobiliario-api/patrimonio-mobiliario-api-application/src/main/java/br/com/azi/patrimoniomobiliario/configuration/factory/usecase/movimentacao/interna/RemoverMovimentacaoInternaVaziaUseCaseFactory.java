package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.RemoverMovimentacaoInternaVaziaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.RemoverMovimentacaoInternaVaziaUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverMovimentacaoInternaVaziaUseCaseFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("RemoverMovimentacaoInternaVaziaUseCase")
    public RemoverMovimentacaoInternaVaziaUseCase createUseCase() {
        return new RemoverMovimentacaoInternaVaziaUseCaseImpl(
            movimentacaoDataProvider
        );
    }

}
