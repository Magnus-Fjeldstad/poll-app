package com.poll.pollapp.config;

import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;

@Component
public class RedisClientProvider {

    private final UnifiedJedis jedis;

    public RedisClientProvider() {
        String host = System.getenv().getOrDefault("SPRING_REDIS_HOST", "localhost");
        String port = System.getenv().getOrDefault("SPRING_REDIS_PORT", "6379");

        String url = "redis://" + host + ":" + port;
        this.jedis = new UnifiedJedis(url);
    }

    public UnifiedJedis getClient() {
        return jedis;
    }
}
