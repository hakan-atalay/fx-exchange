package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class ExchangeRate {
    private Long id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
    private String source;
    private LocalDateTime fetchedAt;

    public ExchangeRate() {}

    public ExchangeRate(Long id, String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate, String source, LocalDateTime fetchedAt) {
        this.id = id;
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
        this.source = source;
        this.fetchedAt = fetchedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
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

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrencyCode, targetCurrencyCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ExchangeRate other = (ExchangeRate) obj;
        return Objects.equals(baseCurrencyCode, other.baseCurrencyCode) &&
               Objects.equals(targetCurrencyCode, other.targetCurrencyCode);
    }

    @Override
    public String toString() {
        return "ExchangeRate [id=" + id + ", baseCurrencyCode=" + baseCurrencyCode +
               ", targetCurrencyCode=" + targetCurrencyCode + ", rate=" + rate +
               ", source=" + source + ", fetchedAt=" + fetchedAt + "]";
    }
}
