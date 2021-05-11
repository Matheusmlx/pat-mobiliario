package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.converter.BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarQuantidadeNotificacoesNaoVisualizadasUseCaseTest {

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    private BuscarQuantidadeNotificacoesNaoVisualizadasUseCase useCase;

    @Before
    public void inicializar() {
        SessaoUsuarioDataProvider sessaoUsuarioDataProvider = new SessaoUsuarioDataProvider() {
            @Override
            public SessaoUsuario get() {
                return SessaoUsuario.builder().id(2L).build();
            }

            @Override
            public String getLogin() {
                return "admin";
            }
        };

        useCase = new BuscarQuantidadeNotificacoesNaoVisualizadasUseCaseImpl(
            notificacaoDataProvider,
            sessaoUsuarioDataProvider,
            new BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter()
        );
    }

    @Test
    public void deveBuscarQuantidadeNotificacoesNaoVisualizadas() {
        when(notificacaoDataProvider.buscarQuantidadeNotificacoesNaoVisualizadas(2L))
            .thenReturn(3L);

        BuscarQuantidadeNotificacoesNaoVisualizadasOutputData outputData = useCase.executar();

        Assert.assertEquals(Long.valueOf(3), outputData.getQuantidadeNaoVisualizadas());
    }

}
