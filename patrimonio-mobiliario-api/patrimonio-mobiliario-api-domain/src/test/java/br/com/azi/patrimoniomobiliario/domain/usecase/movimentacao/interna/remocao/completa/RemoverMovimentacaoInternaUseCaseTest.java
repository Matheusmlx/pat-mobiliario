package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.exception.MovimentacaoNaoEncontradaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class RemoverMovimentacaoInternaUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoNulo() {
        RemoverMovimentacaoInternaUseCase useCase = new RemoverMovimentacaoInternaUseCaseImpl(
            movimentacaoDataProvider,
            documentoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            lancamentoContabilDataProvider
        );

        RemoverMovimentacaoInternaInputData inputData = RemoverMovimentacaoInternaInputData
            .builder()
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        RemoverMovimentacaoInternaUseCase useCase = new RemoverMovimentacaoInternaUseCaseImpl(
            movimentacaoDataProvider,
            documentoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            lancamentoContabilDataProvider
        );

        RemoverMovimentacaoInternaInputData inputData = RemoverMovimentacaoInternaInputData
            .builder()
            .movimentacaoId(5L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test
    public void deveRemoverMovimentacao() {
        RemoverMovimentacaoInternaUseCase useCase = new RemoverMovimentacaoInternaUseCaseImpl(
            movimentacaoDataProvider,
            documentoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            lancamentoContabilDataProvider
        );

        RemoverMovimentacaoInternaInputData inputData = RemoverMovimentacaoInternaInputData
            .builder()
            .movimentacaoId(5L)
            .build();

        NotaLancamentoContabil notaLancamentoContabil = NotaLancamentoContabil
            .builder()
            .id(8L)
            .numero("3432NL123412")
            .dataLancamento(LocalDateTime.now())
            .build();

        Movimentacao movimentacao = Movimentacao
            .builder()
            .id(5L)
            .codigo("00005")
            .usuarioCriacao("admin")
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .notaLancamentoContabil(notaLancamentoContabil)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(movimentacao));

        useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(
            movimentacaoDataProvider,
            documentoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            lancamentoContabilDataProvider);

        inOrder.verify(movimentacaoDataProvider, times(1)).buscarPorId(5L);
        inOrder.verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(5L);
        inOrder.verify(documentoDataProvider, times(1)).removerPorMovimentacao(5L);
        inOrder.verify(lancamentoContabilDataProvider, times(1)).removerPorMovimentacao(5L);
        inOrder.verify(movimentacaoDataProvider, times(1)).remover(movimentacao);
        inOrder.verify(notaLancamentoContabilDataProvider, times(1)).remover(8L);
    }

}
