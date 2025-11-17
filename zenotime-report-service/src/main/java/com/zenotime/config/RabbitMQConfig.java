package com.zenotime.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String EXCHANGE_NAME = "zenotime.exchange";
    public static final String QUEUE_PONTO_REGISTRADO = "ponto.registrado";
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    
    @Bean
    public Queue pontoRegistradoQueue() {
        return QueueBuilder.durable(QUEUE_PONTO_REGISTRADO).build();
    }
    
    @Bean
    public Binding pontoRegistradoBinding() {
        return BindingBuilder
            .bind(pontoRegistradoQueue())
            .to(exchange())
            .with("ponto.registrado");
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

