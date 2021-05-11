package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.distribuicao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.FinalizarIncorporacaoAsyncException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.FinalizarDistribuicaoAsyncException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.FinalizarDistribuicaoErroInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.FinalizarDistribuicaoErroUseCase;
import br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao.finalizar.FinalizarDistribuicaoAmqpErroHandler;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarDistribuicaoAmqpErroHandlerTest {

    @Mock
    private FinalizarDistribuicaoErroUseCase finalizarDistribuicaoErroUseCase;

    @InjectMocks
    private FinalizarDistribuicaoAmqpErroHandler finalizarDistribuicaoAmqpErroHandler;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void deveUtilizarAMensagemDeErroTratadaQuandoACausaDaExcecaoForUmaExcecaoTratada() throws Exception {
        final String amqpMessage = "{\"movimentacaoId\": 1}";
        final Message message = new Message(amqpMessage.getBytes(), null);
        final String mensagemErroTratada = "Mensagem Erro Tratada";
        final ListenerExecutionFailedException exception = new ListenerExecutionFailedException(
            "Mensagem de exceção AMQP",
            new AmqpRejectAndDontRequeueException(mensagemErroTratada, new FinalizarIncorporacaoAsyncException(mensagemErroTratada)),
            message
        );

        final Object output = finalizarDistribuicaoAmqpErroHandler.handleError(message, null, exception);

        Assert.assertNull(output);

        Mockito.verify(finalizarDistribuicaoErroUseCase, Mockito.times(1))
            .executar(FinalizarDistribuicaoErroInputData.builder()
                .movimentacaoId(1L)
                .erroProcessamento(mensagemErroTratada)
                .build());
    }

    @Test
    public void deveUtilizarAMensagemDeErroTratadaQuandoACausaDaExcecaoNaoForUmaExcecaoTratada() throws Exception {
        final String amqpMessage = "{\"movimentacaoId\": 1}";
        final Message message = new Message(amqpMessage.getBytes(), null);
        final ListenerExecutionFailedException exception = new ListenerExecutionFailedException(
            "Mensagem de exceção AMQP",
            new NullPointerException(),
            message
        );

        final Object output = finalizarDistribuicaoAmqpErroHandler.handleError(message, null, exception);

        Assert.assertNull(output);

        Mockito.verify(finalizarDistribuicaoErroUseCase, Mockito.times(1))
            .executar(FinalizarDistribuicaoErroInputData.builder()
                .movimentacaoId(1L)
                .erroProcessamento(FinalizarDistribuicaoAsyncException.DEFAULT_MESSAGE)
                .build());
    }
}
