package cn.comradexy.demo.utils;

import cn.comradexy.demo.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * rabbitmq 工具类
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-17
 * @Description: rabbitmq 工具类
 */
@Component
public class RabbitMQUtils {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送延迟消息
     *
     * @param message 消息
     * @param delay   延迟时间
     */
    public void sendDelayedMessage(String message, long delay) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_QUEUE, message, msg -> {
            // 设置消息的TTL，延迟时间
            msg.getMessageProperties().setExpiration(String.valueOf(delay));
            return msg;
        });
    }

    /**
     * 处理死信消息
     *
     * @param message 消息
     */
    @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE)
    public void handleDeadLetterMessage(String message) {
        System.out.println(LocalDateTime.now() + ": Received delayed message -- [" + message + "]");
    }
}
