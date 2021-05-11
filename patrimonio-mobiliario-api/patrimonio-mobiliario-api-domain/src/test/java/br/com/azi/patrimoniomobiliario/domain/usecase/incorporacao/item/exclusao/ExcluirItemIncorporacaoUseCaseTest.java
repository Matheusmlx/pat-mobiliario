package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.exception.ItemIncorporacaoNaoPodeSerExcluidoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ExcluirItemIncorporacaoUseCaseTest {

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Before
    public void inicializa() {
        itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);
        incorporacaoDataProvider = Mockito.mock(IncorporacaoDataProvider.class);
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoItemIncorporacaoNaoExistir() {
        ExcluirItemIncorporacaoInputData inputData = new ExcluirItemIncorporacaoInputData(1L, 1L);
        ExcluirItemIncorporacaoUseCaseImpl usecase = new ExcluirItemIncorporacaoUseCaseImpl(itemIncorporacaoDataProvider, incorporacaoDataProvider);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        usecase.executar(inputData);
    }

    @Test
    public void deveExcluirItemIncorporacaoQuandoIncorporacaoEstiverEmElaboracao() {
        ExcluirItemIncorporacaoInputData inputData = new ExcluirItemIncorporacaoInputData(1L, 1L);
        ExcluirItemIncorporacaoUseCaseImpl usecase = new ExcluirItemIncorporacaoUseCaseImpl(itemIncorporacaoDataProvider, incorporacaoDataProvider);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .codigo("1554asdf")
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .build()));

        usecase.executar(inputData);

        Mockito.verify(incorporacaoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(itemIncorporacaoDataProvider, Mockito.times(1)).excluir(any(Long.class));
    }

    @Test
    public void deveExcluirItemIncorporacaoQuandoIncorporacaoEstiverComErroProcessamento() {
        ExcluirItemIncorporacaoInputData inputData = new ExcluirItemIncorporacaoInputData(1L, 1L);
        ExcluirItemIncorporacaoUseCaseImpl usecase = new ExcluirItemIncorporacaoUseCaseImpl(itemIncorporacaoDataProvider, incorporacaoDataProvider);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .codigo("1554asdf")
                .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
                .build()));

        usecase.executar(inputData);

        Mockito.verify(incorporacaoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(itemIncorporacaoDataProvider, Mockito.times(1)).excluir(any(Long.class));
    }

    @Test(expected = ItemIncorporacaoNaoPodeSerExcluidoException.class)
    public void deveFalharQuandoIncorporacaoEstiverFinalizado() {
        ExcluirItemIncorporacaoInputData inputData = new ExcluirItemIncorporacaoInputData(1L, 1L);
        ExcluirItemIncorporacaoUseCaseImpl usecase = new ExcluirItemIncorporacaoUseCaseImpl(itemIncorporacaoDataProvider, incorporacaoDataProvider);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .codigo("1554asdf")
                .situacao(Incorporacao.Situacao.FINALIZADO)
                .build()));

        usecase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        ExcluirItemIncorporacaoInputData inputData = new ExcluirItemIncorporacaoInputData();
        ExcluirItemIncorporacaoUseCaseImpl usecase = new ExcluirItemIncorporacaoUseCaseImpl(itemIncorporacaoDataProvider, incorporacaoDataProvider);

        usecase.executar(inputData);
    }

}
