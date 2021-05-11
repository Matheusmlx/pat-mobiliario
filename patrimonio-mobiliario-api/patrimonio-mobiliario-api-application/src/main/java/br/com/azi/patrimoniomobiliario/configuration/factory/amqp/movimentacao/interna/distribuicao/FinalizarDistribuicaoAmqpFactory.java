package br.com.azi.patrimoniomobiliario.configuration.factory.amqp.movimentacao.interna.distribuicao;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FinalizarDistribuicaoAmqpFactory {

    private static final String FINALIZAR_DISTRIBUICAO_QUEUE = "patrimonio-mobiliario.movimentacao.interna.distribuicao.finalizacao";
    private static final String ROUTING_KEY = "movimentacao.interna.distribuicao.finalizacao";

    @Bean("FinalizarDistribuicaoQueue")
    public Queue createQueue() {
        return QueueBuilder.durable(FINALIZAR_DISTRIBUICAO_QUEUE).build();
    }

    @Bean("FinalizarDistribuicaoAmqpRoutingKey")
    public Binding createBinding(@Qualifier("FinalizarDistribuicaoQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY);
    }

}
