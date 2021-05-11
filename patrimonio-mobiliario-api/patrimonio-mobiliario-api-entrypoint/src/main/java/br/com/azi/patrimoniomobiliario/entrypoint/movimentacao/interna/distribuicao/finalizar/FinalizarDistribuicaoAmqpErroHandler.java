package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.FinalizarDistribuicaoAsyncException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.FinalizarDistribuicaoErroInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.FinalizarDistribuicaoErroUseCase;
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
@Component("FinalizarDistribuicaoAmqpErroHandler")
@RequiredArgsConstructor
public class FinalizarDistribuicaoAmqpErroHandler implements RabbitListenerErrorHandler {

    private final FinalizarDistribuicaoErroUseCase finalizarDistribuicaoErroUseCase;

    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException e) throws Exception {
        final FinalizarDistribuicaoMessage finalizarDistribuicaoMessage = converterAmqpMessage(amqpMessage.getBody());

        finalizarDistribuicaoErroUseCase.executar(FinalizarDistribuicaoErroInputData.builder()
            .movimentacaoId(finalizarDistribuicaoMessage.getMovimentacaoId())
            .erroProcessamento(retornarMensagemErro(e))
            .build()
        );

        log.info("[FINALIZAR_DISTRIBUICAO] Distribuição #" + finalizarDistribuicaoMessage.getMovimentacaoId() +
            " finalizada com o seguinte erro: " + e.getCause().getMessage());

        e.printStackTrace();

        return null;
    }

    @Data
    public static class FinalizarDistribuicaoMessage {
        private Long movimentacaoId;
    }

    private FinalizarDistribuicaoMessage converterAmqpMessage(byte[] amqpMessage) {
        return new Gson().fromJson(new String(amqpMessage), FinalizarDistribuicaoMessage.class);
    }

    private String retornarMensagemErro(ListenerExecutionFailedException exception) {
        final Throwable cause = exception.getCause();
        if (Objects.nonNull(cause) && cause instanceof AmqpRejectAndDontRequeueException) {
            return cause.getMessage();
        }
        return FinalizarDistribuicaoAsyncException.DEFAULT_MESSAGE;
    }
}
