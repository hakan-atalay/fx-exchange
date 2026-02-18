package infrastructure.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ApplicationScoped
public class UserLoginCacheService {

    private JedisPool jedisPool;

    private static final String LOGIN_KEY_PREFIX = "user:login:";
    private static final int TTL_SECONDS = 3600;

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

    public Long getCachedUserId(String email) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(LOGIN_KEY_PREFIX + normalize(email));
            return value != null ? Long.parseLong(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public void cacheUserId(String email, Long userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(
                    LOGIN_KEY_PREFIX + normalize(email),
                    TTL_SECONDS,
                    String.valueOf(userId)
            );
        } catch (Exception ignored) {
        }
    }

    public void evict(String email) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(LOGIN_KEY_PREFIX + normalize(email));
        } catch (Exception ignored) {
        }
    }

    private String normalize(String email) {
        return email.toLowerCase();
    }
}
