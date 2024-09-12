package cn.comradexy.demo.utils;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis 工具类
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: redis 工具类
 */
@Component
public class RedisClientUtils {
    @Resource
    private RedissonClient redissonClient;

}
