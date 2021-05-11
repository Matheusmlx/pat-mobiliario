package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.CadastrarMovimentacaoInternaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.CadastrarMovimentacaoInternaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.converter.CadastrarMovimentacaoInternaOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarMovimentacaoInternaFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean({"CadastrarMovimentacaoInternaUseCase"})
    @DependsOn({"CadastrarMovimentacaoInternaOutputDataConverter"})
    public CadastrarMovimentacaoInternaUseCase createUseCase(CadastrarMovimentacaoInternaOutputDataConverter outputDataConverter) {
        return new CadastrarMovimentacaoInternaUseCaseImpl(
            movimentacaoDataProvider,
            sessaoUsuarioDataProvider,
            outputDataConverter
        );
    }

    @Bean({"CadastrarMovimentacaoInternaOutputDataConverter"})
    public CadastrarMovimentacaoInternaOutputDataConverter createOutputDataConverter() {
        return new CadastrarMovimentacaoInternaOutputDataConverter();
    }

}
