package br.com.azi.patrimoniomobiliario.configuration.factory.commons;

import br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando.MemorandoMovimentacaoInternaUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemorandoMovimentacaoInternaUtilsFactory {

    @Bean("MemorandoMovimentacaoInternaUtils")
    public MemorandoMovimentacaoInternaUtils createBean(){
        return new MemorandoMovimentacaoInternaUtils();
    }

}
