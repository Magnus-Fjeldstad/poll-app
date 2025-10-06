package com.poll.pollapp.config;

import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;

@Component
public class RedisClientProvider {

    private final UnifiedJedis jedis;

    public RedisClientProvider() {
        this.jedis = new UnifiedJedis("redis://localhost:6379");
    }

    public UnifiedJedis getClient() {
        return jedis;
    }
}
