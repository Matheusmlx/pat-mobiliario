package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.converter.ExisteIntervalosPorSituacaoOutputDataConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExisteIntervalosPorSituacaoUseCaseTest {

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private ExisteIntervalosPorSituacaoUseCase useCase;

    @Before
    public void inicializarUseCase() {
        useCase = new ExisteIntervalosPorSituacaoUseCaseImpl(
            reservaIntervaloDataProvider,
            new ExisteIntervalosPorSituacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoReservaIdNaoForInformado() {
        useCase.executar(new ExisteIntervalosPorSituacaoInputData());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoSituacaoNaoForInformado() {
        useCase.executar(ExisteIntervalosPorSituacaoInputData.builder()
            .reservaId(1L)
            .build());
    }

    @Test
    public void deveRetornarVerdadeiroQuandoHouverIntervalosEmElaboracao() {
        final ExisteIntervalosPorSituacaoInputData inputData = ExisteIntervalosPorSituacaoInputData.builder()
            .reservaId(1L)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .build();

        final ExisteIntervalosPorSituacaoOutputData outputDataEsperado = ExisteIntervalosPorSituacaoOutputData.builder()
            .reservaId(1L)
            .intervaloSituacao(ReservaIntervalo.Situacao.EM_ELABORACAO.name())
            .existe(true)
            .build();

        when(reservaIntervaloDataProvider.existe(anyLong(), any(ReservaIntervalo.Situacao.class)))
            .thenReturn(true);

        final ExisteIntervalosPorSituacaoOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverIntervalosEmElaboracao() {
        final ExisteIntervalosPorSituacaoInputData inputData = ExisteIntervalosPorSituacaoInputData.builder()
            .reservaId(1L)
            .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
            .build();

        final ExisteIntervalosPorSituacaoOutputData outputDataEsperado = ExisteIntervalosPorSituacaoOutputData.builder()
            .reservaId(1L)
            .intervaloSituacao(ReservaIntervalo.Situacao.EM_ELABORACAO.name())
            .existe(false)
            .build();

        when(reservaIntervaloDataProvider.existe(anyLong(), any(ReservaIntervalo.Situacao.class)))
            .thenReturn(false);

        final ExisteIntervalosPorSituacaoOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
    }
}
