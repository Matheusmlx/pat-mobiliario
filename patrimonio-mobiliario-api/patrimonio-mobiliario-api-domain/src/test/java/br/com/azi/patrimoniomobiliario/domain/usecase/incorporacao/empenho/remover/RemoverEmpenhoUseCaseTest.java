package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remover;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.exception.EmpenhoNaoExisteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.exception.IncorporacaoNaoEstaEmElaboracaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoverEmpenhoUseCaseTest {

    @Mock
    private EmpenhoDataProvider empenhoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    private RemoverEmpenhoUseCase useCase;

    @Before
    public void setUp() {
        useCase = new RemoverEmpenhoUseCaseImpl(empenhoDataProvider, incorporacaoDataProvider);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        useCase.executar(new RemoverEmpenhoInputData());
    }

    @Test
    public void deveRemoverEmpenho() {
        RemoverEmpenhoInputData inputData = new RemoverEmpenhoInputData();
        inputData.setId(2L);

        when(empenhoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Empenho.builder().id(2L).incorporacaoId(3L).build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao.builder().id(3L).situacao(Incorporacao.Situacao.EM_ELABORACAO).build()));

        useCase.executar(inputData);

        verify(empenhoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        verify(empenhoDataProvider, Mockito.times(1)).remover(any(Long.class));
    }

    @Test(expected = EmpenhoNaoExisteException.class)
    public void deveFalharQuandoEmpenhoNaoExistir() {
        RemoverEmpenhoInputData inputData = RemoverEmpenhoInputData.builder().id(2L).build();

        when(empenhoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);

        verify(empenhoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
    }

    @Test(expected = IncorporacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverEmElaboracaoOuComErroProcessamento() {

        when(empenhoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Empenho.builder().id(1L).incorporacaoId(3L).build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao.builder().id(3L).situacao(Incorporacao.Situacao.EM_PROCESSAMENTO).build()));

        useCase.executar(new RemoverEmpenhoInputData(1L));
    }
}
