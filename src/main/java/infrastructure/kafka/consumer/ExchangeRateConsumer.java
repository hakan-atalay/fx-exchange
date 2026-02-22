package infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.ExcangeRateCreateDTO;
import infrastructure.kafka.KafkaTopics;
import infrastructure.kafka.event.ExchangeRateEvent;
import infrastructure.kafka.producer.DeadLetterProducer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import service.ExchangeRateServiceImpl;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@ApplicationScoped
public class ExchangeRateConsumer {

	@Inject
	private ExchangeRateServiceImpl exchangeRateService;

	@Inject
	private DeadLetterProducer deadLetterProducer;

	private KafkaConsumer<String, String> consumer;
	private Thread consumerThread;
	private volatile boolean running = true;
	private final ObjectMapper mapper = new ObjectMapper();

	@PostConstruct
	public void init() {

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "exchange-rate-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(KafkaTopics.EXCHANGE_RATE_EVENTS));

		consumerThread = new Thread(this::poll);
		consumerThread.setName("exchange-rate-consumer-thread");
		consumerThread.start();
	}

	private void poll() {
		try {
			while (running) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

				for (ConsumerRecord<String, String> record : records) {
					try {
						processRecord(record.value());
					} catch (Exception e) {
						deadLetterProducer.send(record.key(), record.value());
						e.printStackTrace();
					}
				}

				if (!records.isEmpty()) {
					consumer.commitSync();
				}
			}
		} catch (WakeupException e) {
			if (running) {
				throw e;
			}
		} finally {
			consumer.close();
		}
	}

	private void processRecord(String value) throws Exception {

		ExchangeRateEvent event = mapper.readValue(value, ExchangeRateEvent.class);

		ExcangeRateCreateDTO dto = new ExcangeRateCreateDTO();
		dto.setBaseCurrency(event.getBaseCurrency());
		dto.setTargetCurrency(event.getTargetCurrency());
		dto.setRate(event.getRate());
		dto.setSource(event.getSource());

		exchangeRateService.createOrUpdateRate(dto);
	}

	@PreDestroy
	public void shutdown() {
		running = false;

		if (consumer != null) {
			consumer.wakeup();
		}

		if (consumerThread != null) {
			try {
				consumerThread.join(5000);
			} catch (InterruptedException ignored) {
			}
		}
	}
}