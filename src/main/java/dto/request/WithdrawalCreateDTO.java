package dto.request;

import java.math.BigDecimal;

public class WithdrawalCreateDTO {

    private String currencyCode;
    private BigDecimal amount;
    private String iban;

    public WithdrawalCreateDTO() {}

    public WithdrawalCreateDTO(String currencyCode, BigDecimal amount, String iban) {
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.iban = iban;
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

    
}
