package infrastructure.kafka.consumer;

import dto.request.ExcangeRateCreateDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import service.ExchangeRateServiceImpl;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@ApplicationScoped
public class ExchangeRateConsumer {

	@Inject
	private ExchangeRateServiceImpl exchangeRateService;

	private KafkaConsumer<String, String> consumer;

	@PostConstruct
	public void init() {

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "exchange-rate-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList("exchange-rate-topic"));

		new Thread(this::poll).start();
	}

	private void poll() {
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

			for (ConsumerRecord<String, String> record : records) {

				String[] parts = record.value().split(",");

				ExcangeRateCreateDTO dto = new ExcangeRateCreateDTO();
				dto.setBaseCurrency(parts[0]);
				dto.setTargetCurrency(parts[1]);
				dto.setRate(new java.math.BigDecimal(parts[2]));
				dto.setSource(parts[3]);

				exchangeRateService.createOrUpdateRate(dto);
			}
		}
	}
}