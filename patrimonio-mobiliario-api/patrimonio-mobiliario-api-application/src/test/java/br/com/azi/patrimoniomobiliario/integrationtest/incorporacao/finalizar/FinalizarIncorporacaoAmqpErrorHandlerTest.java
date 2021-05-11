package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.FinalizarIncorporacaoAsyncException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.FinalizarIncorporacaoErroInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.FinalizarIncorporacaoErroUseCase;
import br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar.FinalizarIncorporacaoAmqpErrorHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarIncorporacaoAmqpErrorHandlerTest {

    @Mock
    private FinalizarIncorporacaoErroUseCase finalizarIncorporacaoErroUseCase;

    @InjectMocks
    private FinalizarIncorporacaoAmqpErrorHandler finalizarIncorporacaoAmqpErrorHandler;

    @Test
    public void deveUtilizarAMensagemDeErroTratadaQuandoACausaDaExcecaoForUmaExcecaoTratada() throws Exception {
        final String amqpMessage = "{\"incorporacaoId\": 1}";
        final Message message = new Message(amqpMessage.getBytes(), null);
        final String mensagemErroTratada = "Mensagem Erro Tratada";
        final ListenerExecutionFailedException exception = new ListenerExecutionFailedException(
            "Mensagem de exceção AMQP",
            new AmqpRejectAndDontRequeueException(mensagemErroTratada, new FinalizarIncorporacaoAsyncException(mensagemErroTratada)),
            message
        );

        final Object output = finalizarIncorporacaoAmqpErrorHandler.handleError(message, null, exception);

        Assert.assertNull(output);

        Mockito.verify(finalizarIncorporacaoErroUseCase, Mockito.times(1))
            .executar(FinalizarIncorporacaoErroInputData.builder()
                .incorporacaoId(1L)
                .erroProcessamento(mensagemErroTratada)
                .build());
    }

    @Test
    public void deveUtilizarAMensagemDeErroTratadaQuandoACausaDaExcecaoNaoForUmaExcecaoTratada() throws Exception {
        final String amqpMessage = "{\"incorporacaoId\": 1}";
        final Message message = new Message(amqpMessage.getBytes(), null);
        final ListenerExecutionFailedException exception = new ListenerExecutionFailedException(
            "Mensagem de exceção AMQP",
            new NullPointerException(),
            message
        );

        final Object output = finalizarIncorporacaoAmqpErrorHandler.handleError(message, null, exception);

        Assert.assertNull(output);

        Mockito.verify(finalizarIncorporacaoErroUseCase, Mockito.times(1))
            .executar(FinalizarIncorporacaoErroInputData.builder()
                .incorporacaoId(1L)
                .erroProcessamento(FinalizarIncorporacaoAsyncException.DEFAULT_MESSAGE)
                .build());
    }
}
