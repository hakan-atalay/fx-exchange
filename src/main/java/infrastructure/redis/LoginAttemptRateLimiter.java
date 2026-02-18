package infrastructure.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ApplicationScoped
public class LoginAttemptRateLimiter {

    private JedisPool jedisPool;

    private static final String KEY_PREFIX = "login:attempt:";
    private static final int MAX_ATTEMPTS = 5;
    private static final int WINDOW_SECONDS = 300;

    @PostConstruct
    public void init() {
        String host = System.getenv().getOrDefault("REDIS_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        config.setMinIdle(1);
        config.setTestOnBorrow(true);

        jedisPool = new JedisPool(config, host, port);
    }

    @PreDestroy
    public void shutdown() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public void checkLimit(String email) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = KEY_PREFIX + normalize(email);
            String value = jedis.get(key);

            if (value != null && Integer.parseInt(value) >= MAX_ATTEMPTS) {
                throw new RuntimeException("Too many login attempts. Try again in 5 minutes.");
            }
        }
    }

    public void recordFailure(String email) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = KEY_PREFIX + normalize(email);
            Long count = jedis.incr(key);

            if (count == 1) {
                jedis.expire(key, WINDOW_SECONDS);
            }
        }
    }

    public void reset(String email) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(KEY_PREFIX + normalize(email));
        }
    }

    private String normalize(String email) {
        return email.toLowerCase();
    }
}
