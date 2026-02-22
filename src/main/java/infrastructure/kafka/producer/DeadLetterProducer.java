package infrastructure.kafka.producer;

import infrastructure.kafka.KafkaTopics;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

@ApplicationScoped
public class DeadLetterProducer {

	private Producer<String, String> producer;

	@PostConstruct
	public void init() {
		Properties props = new Properties();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.ACKS_CONFIG, "all");

		producer = new KafkaProducer<>(props);
	}

	public void send(String key, String message) {
		ProducerRecord<String, String> record = new ProducerRecord<>(KafkaTopics.EXCHANGE_RATE_DLQ, key, message);

		producer.send(record);
	}

	@PreDestroy
	public void close() {
		if (producer != null) {
			producer.close();
		}
	}
}