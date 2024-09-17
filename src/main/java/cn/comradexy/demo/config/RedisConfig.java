package cn.comradexy.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-17
 * @Description: Redis 配置
 */
@Configuration
public class RedisConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);

        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(redisPassword);

        RedissonClient redissonClient = Redisson.create(config);

        logger.info("redissonClient init success");

        return redissonClient;
    }
}
