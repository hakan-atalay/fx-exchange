package infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.kafka.KafkaTopics;
import infrastructure.kafka.event.ExchangeNotificationEvent;
import infrastructure.kafka.producer.DeadLetterProducer;
import infrastructure.mail.MailService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@ApplicationScoped
public class ExchangeNotificationConsumer {

    @Inject
    private MailService mailService;

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
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "exchange-notification-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(KafkaTopics.EXCHANGE_NOTIFICATION_EVENTS));

        consumerThread = new Thread(this::poll);
        consumerThread.setName("exchange-notification-consumer-thread");
        consumerThread.start();
    }

    private void poll() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record : records) {
                    try {
                        process(record.value());
                    } catch (Exception e) {
                        deadLetterProducer.send(record.key(), record.value());
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

    private void process(String value) throws Exception {
        ExchangeNotificationEvent event = mapper.readValue(value, ExchangeNotificationEvent.class);

        String body = String.format(
                "Exchange completed:\n%s %s -> %s %s\nRate: %s",
                event.getAmountFrom(),
                event.getBaseCurrency(),
                event.getAmountTo(),
                event.getTargetCurrency(),
                event.getRate()
        );

        mailService.sendMail(event.getEmail(), "Exchange Successful", body);
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
            } catch (InterruptedException ignored) {}
        }
    }
}