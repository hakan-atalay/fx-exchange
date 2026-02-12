package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Withdrawal {
    private Long id;
    private Long userId;
    private String currencyCode;
    private BigDecimal amount;
    private String iban;
    private String status = "PENDING";
    private LocalDateTime createdAt;

    public Withdrawal() {}

    public Withdrawal(Long id, Long userId, String currencyCode, BigDecimal amount, String iban, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.iban = iban;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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
        return Objects.hash(userId, currencyCode, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Withdrawal other = (Withdrawal) obj;
        return Objects.equals(userId, other.userId) &&
               Objects.equals(currencyCode, other.currencyCode) &&
               Objects.equals(createdAt, other.createdAt);
    }

    @Override
    public String toString() {
        return "Withdrawal [id=" + id + ", userId=" + userId + ", currencyCode=" + currencyCode +
               ", amount=" + amount + ", iban=" + iban + ", status=" + status +
               ", createdAt=" + createdAt + "]";
    }
}
