package com.example.uberprojectlocationservice.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//configuration class is used to define beans
//this class configure how Redis should be connected and used.

//Instead of writing connection logic everywhere,
// we centralize it in one class and let Spring inject it wherever needed (Dependency Injection).
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost ;

    @Value("${spring.data.redis.port}")
    private int redisPort ;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Creates a RedisConnectionFactory that connects to Redis server running on localhost at port 6379.
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    //RedisTemplate is the main class to interact with Redis.
    //It lets you store, retrieve, and manipulate data in Redis
    @Bean
    public RedisTemplate<String , String> redisTemplate(){

        // Create RedisTemplate object to perform Redis operations
        RedisTemplate<String , String> redisTemplate = new RedisTemplate<>();

        // Link RedisTemplate with the connection factory
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Use String serialization for keys and values
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate ;
    }
}
