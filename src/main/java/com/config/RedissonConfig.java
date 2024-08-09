package com.config;/**
 * @author timess
 */


import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @className: RedissonConfig
 * @Version: 1.0
 * @description:
 */

@Configuration
@ConfigurationProperties(prefix = "spring.redis")  //读取yml中已有的配置
@Data
public class RedissonConfig {
    private String host;
    private String port;
    private String password;
    @Bean
    public RedissonClient redisClient(){
        //1.创建配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s",host,port);
        config.useSingleServer().
                setAddress(redisAddress).
                setDatabase(3).
                setPassword(password);
        //创建实例
        return Redisson.create(config);
    }
}
