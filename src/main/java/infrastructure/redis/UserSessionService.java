package infrastructure.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;

@ApplicationScoped
public class UserSessionService {

    private JedisPool jedisPool;

    private static final String SESSION_PREFIX = "session:";
    private static final int TTL_SECONDS = 3600;

    @PostConstruct
    public void init() {
        String host = System.getenv().getOrDefault("REDIS_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        config.setMinIdle(1);

        jedisPool = new JedisPool(config, host, port);
    }

    @PreDestroy
    public void shutdown() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public String createSession(Long userId) {
        String token = UUID.randomUUID().toString();

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(
                    SESSION_PREFIX + token,
                    TTL_SECONDS,
                    String.valueOf(userId)
            );
        }

        return token;
    }

    public Long getUserId(String token) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(SESSION_PREFIX + token);
            return value != null ? Long.parseLong(value) : null;
        }
    }

    public void invalidate(String token) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(SESSION_PREFIX + token);
        }
    }
}
