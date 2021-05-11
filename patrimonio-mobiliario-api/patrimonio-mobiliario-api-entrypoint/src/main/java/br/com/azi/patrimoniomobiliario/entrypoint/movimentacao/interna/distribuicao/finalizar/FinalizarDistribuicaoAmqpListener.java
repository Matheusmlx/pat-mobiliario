package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.FinalizarIncorporacaoAsyncException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.FinalizarDistribuicaoAsyncInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.FinalizarDistribuicaoAsyncUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.FinalizarDistribuicaoAsyncException;
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
public class FinalizarDistribuicaoAmqpListener {

    private static final String FINALIZAR_DISTRIBUICAO_QUEUE = "patrimonio-mobiliario.movimentacao.interna.distribuicao.finalizacao";
    private static final String ERRO_HANDLER = "FinalizarDistribuicaoAmqpErroHandler";

    private final FinalizarDistribuicaoAsyncUseCase finalizarDistribuicaoAsyncUseCase;

    @Transactional
    @RabbitListener(queues = FINALIZAR_DISTRIBUICAO_QUEUE, errorHandler = ERRO_HANDLER)
    public void handleMessage(String amqpMessage) {
        log.info("[FINALIZAR_DISTRIBUICAO] Mensagem recebida: " + amqpMessage);

        final FinalizarDistribuicaoAmqpListener.FinalizarDistribuicaoMessage message = converterAmqpMessage(amqpMessage);
        log.info("[FINALIZAR_DISTRIBUICAO] Iniciando a finalização da distribuição #" + message.getMovimentacaoId());

        try {
            finalizarDistribuicaoAsyncUseCase.executar(new FinalizarDistribuicaoAsyncInputData(message.getMovimentacaoId()));
            log.info("[FINALIZAR_DISTRIBUICAO] Distribuição #" + message.getMovimentacaoId() + " finalizada com sucesso");
        } catch (FinalizarDistribuicaoAsyncException e) {
            log.info("[FINALIZAR_DISTRIBUICAO] Erro ao processar #" + message.getMovimentacaoId() + ": " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e.getMessage(), e);
        } catch (Exception e) {
            log.info("[FINALIZAR_DISTRIBUICAO] Erro ao processar #" + message.getMovimentacaoId() + ": " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException(FinalizarIncorporacaoAsyncException.DEFAULT_MESSAGE, e);
        }
    }

    private FinalizarDistribuicaoAmqpListener.FinalizarDistribuicaoMessage converterAmqpMessage(String amqpMessage) {
        return new Gson().fromJson(amqpMessage, FinalizarDistribuicaoAmqpListener.FinalizarDistribuicaoMessage.class);
    }

    @Data
    public static class FinalizarDistribuicaoMessage {
        private Long movimentacaoId;
    }

}
