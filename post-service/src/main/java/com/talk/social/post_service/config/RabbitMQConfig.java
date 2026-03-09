package com.talk.social.post_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "notification_queue";
    public static final String EXCHANGE = "notification_exchange";
    public static final String ROUTING_KEY = "notification_routing_key";

    // 1. Define the Queue (Where messages sit)
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true); // true = durable (survives broker restart)
    }

    // 2. Define the Exchange (The post office that routes messages)
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    // 3. Binding the Queue to the Exchange via the Routing Key
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // 4. Critical: Convert Java Objects/Records to JSON automatically
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}