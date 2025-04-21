package cz.sinko.exchangerates.integration.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

/**
 * Cache configuration for Redis.
 *
 * @author Radovan Šinko
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    /**
     * Redis connection factory.
     *
     * @return Redis connection factory
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        Assert.notNull(redisProperties.getHost(), "Redis host must not be null");
        Assert.isTrue(redisProperties.getPort() > 0, "Redis port must be filled");

        final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    /**
     * Redis template.
     *
     * @return Redis template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
