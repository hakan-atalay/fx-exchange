package entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Wallet {
    private Long id;
    private Long userId;
    private String currencyCode;
    private BigDecimal balance = BigDecimal.ZERO;

    public Wallet() {}

    public Wallet(Long id, Long userId, String currencyCode, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.currencyCode = currencyCode;
        this.balance = balance;
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

	public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currencyCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Wallet other = (Wallet) obj;
        return Objects.equals(userId, other.userId) &&
               Objects.equals(currencyCode, other.currencyCode);
    }

    @Override
    public String toString() {
        return "Wallet [id=" + id + ", userId=" + userId +
               ", currencyId=" + currencyCode + ", balance=" + balance + "]";
    }
}