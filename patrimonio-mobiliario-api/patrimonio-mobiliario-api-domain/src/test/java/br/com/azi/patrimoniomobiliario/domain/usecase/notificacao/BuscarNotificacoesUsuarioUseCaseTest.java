package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter.BuscarNotificacoesUsuarioFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter.BuscarNotificacoesUsuarioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarNotificacoesUsuarioUseCaseTest {

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    private BuscarNotificacoesUsuarioUseCase useCase;

    @Before
    public void inicializa() {
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

        useCase = new BuscarNotificacoesUsuarioUseCaseImpl(
            notificacaoDataProvider,
            new BuscarNotificacoesUsuarioFiltroConverter(),
            new BuscarNotificacoesUsuarioOutputDataConverter(),
            sessaoUsuarioDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoFiltroIncompleto() {
        BuscarNotificacoesUsuarioInputData inputData = BuscarNotificacoesUsuarioInputData
            .builder()
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarNotificacoesUsuario() {
        BuscarNotificacoesUsuarioInputData inputData = BuscarNotificacoesUsuarioInputData
            .builder()
            .page(1L)
            .build();

        Notificacao.Filtro filtro = Notificacao
            .Filtro
            .builder()
            .usuario(2L)
            .build();

        LocalDateTime dataCriacao = LocalDateTime.now();

        List<Notificacao> notificacoes = List.of(
            Notificacao
            .builder()
            .id(1L)
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(3L)
            .assunto("Distribuição 1234567")
            .mensagem("Em Elaboração")
            .dataCriacao(dataCriacao)
            .visualizada(false)
            .usuario(
                Usuario
                    .builder()
                    .id(2L)
                    .build()
            )
            .build());

        filtro.setPage(0L);

        when(notificacaoDataProvider.buscarPorFiltro(any(Notificacao.Filtro.class)))
            .thenReturn(
                ListaPaginada
                    .<Notificacao>builder()
                    .items(notificacoes)
                    .totalPages(1L)
                    .totalElements(1L)
                    .build()
            );

        when(notificacaoDataProvider.buscarQuantidadeNotificacoesNaoVisualizadas(anyLong()))
            .thenReturn(1L);

        BuscarNotificacoesUsuarioOutputData outputData = useCase.executar(inputData);

        verify(notificacaoDataProvider, times(1)).buscarPorFiltro(filtro);
        verify(notificacaoDataProvider, times(1)).atualizarNotificacoes(notificacoes);
        Assert.assertTrue(notificacoes.get(0).getVisualizada());

        Assert.assertEquals("DISTRIBUICAO", outputData.getItems().get(0).getOrigem());
        Assert.assertEquals(Long.valueOf(3L), outputData.getItems().get(0).getOrigemId());
        Assert.assertEquals("Distribuição 1234567", outputData.getItems().get(0).getAssunto());
        Assert.assertEquals("Em Elaboração", outputData.getItems().get(0).getMensagem());
        Assert.assertEquals(DateValidate.formatarData(dataCriacao), outputData.getItems().get(0).getDataCriacao());
        Assert.assertEquals(Long.valueOf(0L), outputData.getQuantidadeNaoVisualizadas());
        Assert.assertFalse(outputData.getItems().get(0).getVisualizada());
    }

}
