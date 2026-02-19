package dto.request;

import java.math.BigDecimal;

public class ExchangeCreateDTO {

    private String fromCurrencyCode;
    private String toCurrencyCode;
    private BigDecimal amount;

    public ExchangeCreateDTO() {}

    public ExchangeCreateDTO(String fromCurrencyCode,
                              String toCurrencyCode,
                              BigDecimal amount) {
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.amount = amount;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
