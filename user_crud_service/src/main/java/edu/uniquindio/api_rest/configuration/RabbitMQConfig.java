package edu.uniquindio.api_rest.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "notifications_queue";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true); // `true` para que sea duradera
    }
}
