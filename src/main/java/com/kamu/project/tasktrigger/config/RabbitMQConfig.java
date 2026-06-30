package com.kamu.project.tasktrigger.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //Kuyruk ve Exchange isimlerini sabit (const) tanimliyoruz her yerde ayni olsun
    public static final String QUEUE_NAME = "task_queue";
    public static final String EXCHANGE_NAME = "task_exchange";
    public static final String ROUTING_KEY = "task_routing_key";

    //1. Kuyrugumuzu olusturuyoruz (true = durable: sunucu cokse bile mesajlar kaybolmaz, diske yazilir)
    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME, true);
    }
    //2. Echange olusturuyoruz  (Mesajlari alip dogru kuyruga yonelendiren santral)
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    //3. Kuyruk ve Exchange'i birbirine bagliyoruz
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    //4. Mesajlari java opbjesij yerine JSON formatinda gondermek icin donusturucu
    @Bean
    @SuppressWarnings("deprecation")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myWorkerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }


}
