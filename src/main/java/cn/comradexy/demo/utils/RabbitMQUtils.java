package cn.comradexy.demo.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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


}
