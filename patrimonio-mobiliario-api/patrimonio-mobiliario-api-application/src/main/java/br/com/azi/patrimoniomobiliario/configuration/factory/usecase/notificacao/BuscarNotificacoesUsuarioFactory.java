package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.notificacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter.BuscarNotificacoesUsuarioFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter.BuscarNotificacoesUsuarioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarNotificacoesUsuarioFactory {

    @Autowired
    private NotificacaoDataProvider notificacaoDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("BuscarNotificacoesUsuarioUseCase")
    @DependsOn({"BuscarNotificacoesUsuarioFiltroConverter", "BuscarNotificacoesUsuarioOutputDataConverter"})
    public BuscarNotificacoesUsuarioUseCase createUseCase(BuscarNotificacoesUsuarioFiltroConverter filtroConverter,
                                                          BuscarNotificacoesUsuarioOutputDataConverter outputDataConverter){
        return new BuscarNotificacoesUsuarioUseCaseImpl(
            notificacaoDataProvider,
            filtroConverter,
            outputDataConverter,
            sessaoUsuarioDataProvider
        );
    }

    @Bean("BuscarNotificacoesUsuarioOutputDataConverter")
    public BuscarNotificacoesUsuarioOutputDataConverter createOutputDataConverter(){
        return new BuscarNotificacoesUsuarioOutputDataConverter();
    }

    @Bean("BuscarNotificacoesUsuarioFiltroConverter")
    public BuscarNotificacoesUsuarioFiltroConverter createFiltroConverter(){
        return new BuscarNotificacoesUsuarioFiltroConverter();
    }

}
