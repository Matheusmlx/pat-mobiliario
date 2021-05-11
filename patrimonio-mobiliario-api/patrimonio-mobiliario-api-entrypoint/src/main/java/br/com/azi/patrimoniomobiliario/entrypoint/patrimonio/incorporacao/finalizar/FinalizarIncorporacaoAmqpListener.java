package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.FinalizarIncorporacaoAsyncInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.FinalizarIncorporacaoAsyncUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.FinalizarIncorporacaoAsyncException;
import com.google.gson.Gson;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class FinalizarIncorporacaoAmqpListener {

    private static final String FINALIZAR_INCORPORACAO_QUEUE = "patrimonio-mobiliario.incorporacao.finalizacao";
    private static final String ERRO_HANDLER = "FinalizarIncorporacaoAmqpErrorHandler";

    private final FinalizarIncorporacaoAsyncUseCase finalizarIncorporacaoAsyncUseCase;

    @Transactional
    @RabbitListener(queues = FINALIZAR_INCORPORACAO_QUEUE, errorHandler = ERRO_HANDLER)
    public void handleMessage(String amqpMessage) {
        log.info("[FINALIZAR_INCORPORACAO] Mensagem recebida: " + amqpMessage);

        final FinalizarIncorporacaoMessage message = converterAmqpMessage(amqpMessage);
        log.info("[FINALIZAR_INCORPORACAO] Iniciando a finalização da incorporação #" + message.getIncorporacaoId());

        try {
            finalizarIncorporacaoAsyncUseCase.executar(new FinalizarIncorporacaoAsyncInputData(message.getIncorporacaoId()));
            log.info("[FINALIZAR_INCORPORACAO] Incorporação #" + message.getIncorporacaoId() + " finalizada com sucesso");
        } catch (FinalizarIncorporacaoAsyncException e) {
            log.info("[FINALIZAR_INCORPORACAO] Erro ao processar #" + message.getIncorporacaoId() + ": " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e.getMessage(), e);
        } catch (Exception e) {
            log.info("[FINALIZAR_INCORPORACAO] Erro ao processar #" + message.getIncorporacaoId() + ": " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException(FinalizarIncorporacaoAsyncException.DEFAULT_MESSAGE, e);
        }
    }

    private FinalizarIncorporacaoMessage converterAmqpMessage(String amqpMessage) {
        return new Gson().fromJson(amqpMessage, FinalizarIncorporacaoMessage.class);
    }

    @Data
    public static class FinalizarIncorporacaoMessage {
        private Long incorporacaoId;
    }
}
