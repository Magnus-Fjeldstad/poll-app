package com.poll.pollapp.cache;

import com.poll.pollapp.config.RedisClientProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PollVoteCache {
    private static final int TTL_SECONDS = 300;

    private final UnifiedJedis jedis;

    @Autowired
    public PollVoteCache(RedisClientProvider provider) {
        this.jedis = provider.getClient();
    }

    public Map<String, String> getVotes(UUID pollId) {
        String key = getKey(pollId);
        return jedis.hgetAll(key); // Redis Hash
    }

    public void putVotes(UUID pollId, Map<Integer, Long> votes) {
        String key = getKey(pollId);
        Map<String, String> stringMap = votes.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue().toString()
                ));
        jedis.hset(key, stringMap);
        jedis.expire(key, TTL_SECONDS);
    }

    public void invalidate(UUID pollId) {
        jedis.del(getKey(pollId));
    }

    private String getKey(UUID pollId) {
        return "poll:" + pollId + ":votes";
    }
}
