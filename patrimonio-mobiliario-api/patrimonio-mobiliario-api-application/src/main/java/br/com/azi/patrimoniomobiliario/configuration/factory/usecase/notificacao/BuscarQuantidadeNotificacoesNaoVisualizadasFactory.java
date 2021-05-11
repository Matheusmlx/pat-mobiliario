package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.notificacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.converter.BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarQuantidadeNotificacoesNaoVisualizadasFactory {

    @Autowired
    private NotificacaoDataProvider notificacaoDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("BuscarQuantidadeNotificacoesNaoVisualizadasUseCase")
    @DependsOn({"BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter"})
    public BuscarQuantidadeNotificacoesNaoVisualizadasUseCase createUseCase(BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter outputDataConverter) {
        return new BuscarQuantidadeNotificacoesNaoVisualizadasUseCaseImpl(
            notificacaoDataProvider,
            sessaoUsuarioDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter")
    public BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter createOutputDataConverter() {
        return new BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter();
    }
}
