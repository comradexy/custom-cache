package cn.comradexy.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-17
 * @Description: RabbitMQ 配置
 */
@Configuration
public class RabbitMQConfig {
    // 正常队列
    public static final String DELAY_QUEUE = "delay.queue";
    // 死信队列
    public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";
    // 死信交换器
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";

    // 正常队列配置
    @Bean(name = DELAY_QUEUE)
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)  // 指定死信交换器
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_QUEUE)  // 指定死信队列路由键
                .build();
    }

    // 死信队列
    @Bean(name = DEAD_LETTER_QUEUE)
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    // 死信交换器
    @Bean(name = DEAD_LETTER_EXCHANGE)
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 绑定死信队列到死信交换器
    @Bean(name = "deadLetterBinding")
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_QUEUE);
    }
}
