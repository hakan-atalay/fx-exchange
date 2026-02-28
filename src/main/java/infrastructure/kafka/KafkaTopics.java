package infrastructure.kafka;

public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String EXCHANGE_RATE_EVENTS = "exchange-rate-events";
    public static final String EXCHANGE_RATE_DLQ = "exchange-rate-dlq";

    public static final String EXCHANGE_NOTIFICATION_EVENTS = "exchange-notification-events";
    public static final String EXCHANGE_NOTIFICATION_DLQ = "exchange-notification-dlq";
}