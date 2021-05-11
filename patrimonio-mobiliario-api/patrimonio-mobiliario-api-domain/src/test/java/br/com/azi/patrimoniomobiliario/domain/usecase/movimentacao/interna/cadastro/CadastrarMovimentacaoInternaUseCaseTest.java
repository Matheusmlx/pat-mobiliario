package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.converter.CadastrarMovimentacaoInternaOutputDataConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarMovimentacaoInternaUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoTiverTipoMovimentacao() {
        CadastrarMovimentacaoInternaUseCase useCase = new CadastrarMovimentacaoInternaUseCaseImpl(
            movimentacaoDataProvider,
            sessaoUsuarioDataProvider,
            new CadastrarMovimentacaoInternaOutputDataConverter()
        );

        CadastrarMovimentacaoInternaInputData inputData = CadastrarMovimentacaoInternaInputData.builder().build();

        useCase.executar(inputData);
    }

    @Test
    public void deveRealizarCadastroDistribuicao() {
        CadastrarMovimentacaoInternaUseCase useCase = new CadastrarMovimentacaoInternaUseCaseImpl(
            movimentacaoDataProvider,
            sessaoUsuarioDataProvider,
            new CadastrarMovimentacaoInternaOutputDataConverter()
        );

        CadastrarMovimentacaoInternaInputData inputData = CadastrarMovimentacaoInternaInputData
            .builder()
            .tipo("DISTRIBUICAO")
            .build();

        Movimentacao movimentacao = Movimentacao
            .builder()
            .codigo("00002")
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .usuarioCriacao("admin")
            .build();

        Mockito.when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado())
            .thenReturn("00001");

        Mockito.when(sessaoUsuarioDataProvider.getLogin())
            .thenReturn("admin");

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao
                .builder()
                .id(2L)
                .codigo("00002")
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .usuarioCriacao("admin")
                .build());

        CadastrarMovimentacaoInternaOutputData outputData = useCase.executar(inputData);

        verify(sessaoUsuarioDataProvider, times(1)).getLogin();
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);

        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("00002", outputData.getCodigo());
        assertEquals("DISTRIBUICAO", outputData.getTipo());
        assertEquals("EM_ELABORACAO", outputData.getSituacao());
        assertEquals("admin", outputData.getUsuarioCriacao());
    }

}
