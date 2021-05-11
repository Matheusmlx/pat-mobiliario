package br.com.azi.patrimoniomobiliario.configuration.factory.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmqpConfiguration {

    public static final String TOPIC_EXCHANGE_NAME = "patrimonio-mobiliario-exchange";

    @Bean
    public TopicExchange createTopicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }
}
