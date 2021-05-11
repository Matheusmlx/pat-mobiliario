package br.com.azi.patrimoniomobiliario.configuration.factory.amqp.incorporacao;

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
public class FinalizarIncorporacaoAmqpFactory {

    private static final String FINALIZAR_INCORPORACAO_QUEUE = "patrimonio-mobiliario.incorporacao.finalizacao";
    private static final String ROUTING_KEY = "incorporacao.finalizacao";

    @Bean("FinalizarIncorporacaoQueue")
    public Queue createQueue() {
        return QueueBuilder.durable(FINALIZAR_INCORPORACAO_QUEUE).build();
    }

    @Bean("FinalizarIncorporacaoAmqpRoutingKey")
    public Binding createBinding(@Qualifier("FinalizarIncorporacaoQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY);
    }

}
