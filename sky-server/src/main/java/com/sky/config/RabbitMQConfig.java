@Configuration
public class RabbitMQConfig {
    
    @Bean
    public Exchange orderExchange() {
        return ExchangeBuilder.directExchange("order.exchange")
                .durable(true)
                .build();
    }
    
    @Bean
    public Queue orderPaySuccessQueue() {
        return QueueBuilder.durable("order.pay.success.queue")
                .build();
    }
    
    @Bean
    public Binding orderPaySuccessBinding() {
        return BindingBuilder.bind(orderPaySuccessQueue())
                .to(orderExchange())
                .with("order.pay.success")
                .noargs();
    }
}