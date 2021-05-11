package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.FinalizarIncorporacaoAsyncInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.FinalizarIncorporacaoAsyncUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.FinalizarIncorporacaoAsyncException;
import br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar.FinalizarIncorporacaoAmqpListener;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarIncorporacaoAmqpListenerTest {

    @Mock
    private FinalizarIncorporacaoAsyncUseCase finalizarIncorporacaoAsyncUseCase;

    @InjectMocks
    private FinalizarIncorporacaoAmqpListener finalizarIncorporacaoAmqpListener;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void deveChamarCasoDeUsoParaProcessarAIncorporacao() {
        final String amqpMessage = "{\"incorporacaoId\": 1}";

        finalizarIncorporacaoAmqpListener.handleMessage(amqpMessage);

        Mockito.verify(finalizarIncorporacaoAsyncUseCase, Mockito.times(1))
            .executar(new FinalizarIncorporacaoAsyncInputData(1L));
    }

    @Test
    public void deveLancarExcecaoParaNaoProcessarAIncorporacaoNovamenteQuandoOProcessamentoFalharComExcecaoTratada() {
        final String amqpMessage = "{\"incorporacaoId\": 1}";
        final FinalizarIncorporacaoAsyncException exception = new FinalizarIncorporacaoAsyncException("Test");

        Mockito.doThrow(exception).when(finalizarIncorporacaoAsyncUseCase)
            .executar(Mockito.any(FinalizarIncorporacaoAsyncInputData.class));

        exceptionRule.expect(AmqpRejectAndDontRequeueException.class);
        exceptionRule.expectCause(IsEqual.equalTo(exception));

        finalizarIncorporacaoAmqpListener.handleMessage(amqpMessage);
    }

    @Test
    public void deveLancarExcecaoParaNaoProcessarAIncorporacaoNovamenteQuandoOProcessamentoFalharComExcecaoNaoTratada() {
        final String amqpMessage = "{\"incorporacaoId\": 1}";
        final Exception exception = new NullPointerException("Test");

        Mockito.doThrow(exception).when(finalizarIncorporacaoAsyncUseCase)
            .executar(Mockito.any(FinalizarIncorporacaoAsyncInputData.class));

        exceptionRule.expect(AmqpRejectAndDontRequeueException.class);
        exceptionRule.expectCause(IsEqual.equalTo(exception));

        finalizarIncorporacaoAmqpListener.handleMessage(amqpMessage);
    }
}
