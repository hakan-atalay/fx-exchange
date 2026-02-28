package infrastructure.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.kafka.KafkaTopics;
import infrastructure.kafka.event.ExchangeNotificationEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

@ApplicationScoped
public class ExchangeNotificationProducer {

	private KafkaProducer<String, String> producer;
	private final ObjectMapper mapper = new ObjectMapper();

	@PostConstruct
	public void init() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 3);

		producer = new KafkaProducer<>(props);
	}

	public void send(ExchangeNotificationEvent event) {
		try {
			String json = mapper.writeValueAsString(event);
			ProducerRecord<String, String> record = new ProducerRecord<>(KafkaTopics.EXCHANGE_NOTIFICATION_EVENTS,
					event.getEmail(), json);

			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}