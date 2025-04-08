package com.sky.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange", true, false);
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue orderPaymentQueue() {
        return QueueBuilder.durable("order.payment.queue")
                .build();
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding orderPaymentBinding() {
        return BindingBuilder.bind(orderPaymentQueue())
                .to(orderExchange())
                .with("order.pay.success");
    }
    
    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        
        // 消息发送失败返回队列中
        rabbitTemplate.setMandatory(true);
        
        // 消息发送到交换机确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println("消息发送到交换机失败: " + cause);
            }
        });
        
        // 消息从交换机发送到队列失败回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("消息从交换机发送到队列失败: " + replyText);
        });
        
        return rabbitTemplate;
    }
}