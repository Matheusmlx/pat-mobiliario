package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.converter.BuscarSituacaoIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.exception.IncorporacaoNaoEncontradaException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarSituacaoIncorporacaoUseCaseTest {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Before
    public void inicializa() {
        incorporacaoDataProvider = Mockito.mock(IncorporacaoDataProvider.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdEhNulo() {
        BuscarSituacaoIncorporacaoUseCase useCase = new BuscarSituacaoIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            new BuscarSituacaoIncorporacaoOutputDataConverter()
        );

        BuscarSituacaoIncorporacaoInputData inputData = BuscarSituacaoIncorporacaoInputData.builder().build();

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoIncorporacaoNaoEncontrada() {
        BuscarSituacaoIncorporacaoUseCase useCase = new BuscarSituacaoIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            new BuscarSituacaoIncorporacaoOutputDataConverter()
        );

        BuscarSituacaoIncorporacaoInputData inputData = BuscarSituacaoIncorporacaoInputData
            .builder()
            .incorporacaoId(3L)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarSituacaoIncorporacao() {
        BuscarSituacaoIncorporacaoUseCase useCase = new BuscarSituacaoIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            new BuscarSituacaoIncorporacaoOutputDataConverter()
        );

        BuscarSituacaoIncorporacaoInputData inputData = new BuscarSituacaoIncorporacaoInputData();
        inputData.setIncorporacaoId(1L);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .codigo("1554asdf")
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .build()));

        BuscarSituacaoIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }
}
