package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinalizarIncorporacaoAmqpSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Long incorporacaoId) {
        final String message = new Gson().toJson(new FinalizarIncorporacaoMessage(incorporacaoId));
        rabbitTemplate.convertAndSend("patrimonio-mobiliario-exchange", "incorporacao.finalizacao", message);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinalizarIncorporacaoMessage {
        private Long incorporacaoId;
    }

}
