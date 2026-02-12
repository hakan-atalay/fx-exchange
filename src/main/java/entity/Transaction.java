package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Long id;
    private Long userId;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private BigDecimal amountFrom;
    private BigDecimal amountTo;
    private BigDecimal exchangeRate;
    private String transactionType;
    private String status = "COMPLETED";
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(Long id, Long userId, String fromCurrencyCode, String toCurrencyCode,
                       BigDecimal amountFrom, BigDecimal amountTo, BigDecimal exchangeRate,
                       String transactionType, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
        this.exchangeRate = exchangeRate;
        this.transactionType = transactionType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }

    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }

    public BigDecimal getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(BigDecimal amountFrom) {
        this.amountFrom = amountFrom;
    }

    public BigDecimal getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(BigDecimal amountTo) {
        this.amountTo = amountTo;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, fromCurrencyCode, toCurrencyCode, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        return Objects.equals(userId, other.userId) &&
               Objects.equals(fromCurrencyCode, other.fromCurrencyCode) &&
               Objects.equals(toCurrencyCode, other.toCurrencyCode) &&
               Objects.equals(createdAt, other.createdAt);
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", userId=" + userId +
               ", fromCurrencyCode=" + fromCurrencyCode + ", toCurrencyCode=" + toCurrencyCode +
               ", amountFrom=" + amountFrom + ", amountTo=" + amountTo +
               ", exchangeRate=" + exchangeRate + ", transactionType=" + transactionType +
               ", status=" + status + ", createdAt=" + createdAt + "]";
    }
}
