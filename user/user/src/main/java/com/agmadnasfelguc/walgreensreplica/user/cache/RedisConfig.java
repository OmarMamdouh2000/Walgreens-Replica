package com.agmadnasfelguc.walgreensreplica.user.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.protocol.ProtocolVersion;

@Configuration
public class RedisConfig {

    @Primary
    @Bean(name= "sessionRedisConnectionFactory")
    public LettuceConnectionFactory sessionRedisConnectionFactory() {
        return buildConnectionFactory("localhost", 6379, "root");
    }

    @Bean(name = "otpRedisConnectionFactory")
    public LettuceConnectionFactory otpRedisConnectionFactory() {
        return buildConnectionFactory("localhost", 6380, "otp_secret");
    }

    private LettuceConnectionFactory buildConnectionFactory(String host, int port, String password) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .clientOptions(ClientOptions.builder().protocolVersion(ProtocolVersion.RESP2).build())
                .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean(name = "sessionRedisTemplate")
    public RedisTemplate<String, Object> sessionRedisTemplate(@Autowired @Qualifier("sessionRedisConnectionFactory") RedisConnectionFactory sessionRedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(sessionRedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean(name = "otpRedisTemplate")
    public RedisTemplate<String, Object> otpRedisTemplate(@Autowired @Qualifier("otpRedisConnectionFactory") RedisConnectionFactory otpRedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(otpRedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

}
