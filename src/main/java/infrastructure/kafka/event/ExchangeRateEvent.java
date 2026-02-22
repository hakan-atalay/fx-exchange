package infrastructure.kafka.event;

import java.math.BigDecimal;

public class ExchangeRateEvent {

    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;
    private String source;

    public ExchangeRateEvent() {}

    public ExchangeRateEvent(String baseCurrency, String targetCurrency, BigDecimal rate, String source) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.source = source;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}