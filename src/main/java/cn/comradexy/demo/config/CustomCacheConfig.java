package cn.comradexy.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * config demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: config demo
 */
@Configuration
public class CustomCacheConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);

        config.useSingleServer().setAddress("redis://localhost:6379");

        RedissonClient redissonClient = Redisson.create(config);

        logger.info("redissonClient init success");

        return redissonClient;
    }

}
