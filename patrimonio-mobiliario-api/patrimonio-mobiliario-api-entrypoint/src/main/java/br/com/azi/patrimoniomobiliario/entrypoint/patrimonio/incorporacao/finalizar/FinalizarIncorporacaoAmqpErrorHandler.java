package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.FinalizarIncorporacaoAsyncException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.FinalizarIncorporacaoErroInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.FinalizarIncorporacaoErroUseCase;
import com.google.gson.Gson;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log4j2
@Component("FinalizarIncorporacaoAmqpErrorHandler")
@RequiredArgsConstructor
public class FinalizarIncorporacaoAmqpErrorHandler implements RabbitListenerErrorHandler {

    private final FinalizarIncorporacaoErroUseCase finalizarIncorporacaoErroUseCase;

    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException e) throws Exception {
        final FinalizarIncorporacaoMessage finalizarIncorporacaoMessage = converterAmqpMessage(amqpMessage.getBody());

        finalizarIncorporacaoErroUseCase.executar(FinalizarIncorporacaoErroInputData.builder()
            .incorporacaoId(finalizarIncorporacaoMessage.getIncorporacaoId())
            .erroProcessamento(retornarMensagemErro(e))
            .build()
        );

        log.info("[FINALIZAR_INCORPORACAO] Incorporação #" + finalizarIncorporacaoMessage.getIncorporacaoId() +
            " finalizada com o seguinte erro: " + e.getCause().getMessage());

        e.printStackTrace();

        return null;
    }

    @Data
    public static class FinalizarIncorporacaoMessage {
        private Long incorporacaoId;
    }

    private FinalizarIncorporacaoMessage converterAmqpMessage(byte[] amqpMessage) {
        return new Gson().fromJson(new String(amqpMessage), FinalizarIncorporacaoMessage.class);
    }

    private String retornarMensagemErro(ListenerExecutionFailedException exception) {
        final Throwable cause = exception.getCause();
        if (Objects.nonNull(cause) && cause instanceof AmqpRejectAndDontRequeueException) {
            return cause.getMessage();
        }
        return FinalizarIncorporacaoAsyncException.DEFAULT_MESSAGE;
    }
}
