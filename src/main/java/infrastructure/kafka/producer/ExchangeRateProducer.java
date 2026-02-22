package infrastructure.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.kafka.event.ExchangeRateEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

@ApplicationScoped
public class ExchangeRateProducer {

	private Producer<String, String> producer;
	private final ObjectMapper mapper = new ObjectMapper();
	private static final String TOPIC = "exchange-rate-events";

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

	public void send(ExchangeRateEvent event) {
		try {
			String json = mapper.writeValueAsString(event);
			ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, event.getTargetCurrency(), json);
			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void close() {
		if (producer != null)
			producer.close();
	}
}