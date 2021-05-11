package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao.finalizar;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinalizarDistribuicaoAmqpSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Long movimentacaoId) {
        rabbitTemplate.convertAndSend(
            "patrimonio-mobiliario-exchange",
            "movimentacao.interna.distribuicao.finalizacao",
            converterMensagem(movimentacaoId)
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinalizarDistribuicaoMessage {
        private Long movimentacaoId;
    }

    private String converterMensagem(Long movimentacaoId) {
        return new Gson().toJson(new FinalizarDistribuicaoMessage(movimentacaoId));
    }
}
