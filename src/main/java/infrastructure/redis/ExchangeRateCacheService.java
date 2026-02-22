package infrastructure.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.math.BigDecimal;

@ApplicationScoped
public class ExchangeRateCacheService {

	private JedisPool jedisPool;

	private static final String KEY_PREFIX = "fx:rate:";
	private static final int TTL_SECONDS = 300;

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

	private String key(String base, String target) {
		return KEY_PREFIX + base + ":" + target;
	}

	public BigDecimal get(String base, String target) {
		try (Jedis jedis = jedisPool.getResource()) {
			String value = jedis.get(key(base, target));
			return value != null ? new BigDecimal(value) : null;
		} catch (Exception e) {
			return null;
		}
	}

	public void put(String base, String target, BigDecimal rate) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.setex(key(base, target), TTL_SECONDS, rate.toPlainString());
		} catch (Exception ignored) {
		}
	}

	public void evict(String base, String target) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.del(key(base, target));
		} catch (Exception ignored) {
		}
	}
}