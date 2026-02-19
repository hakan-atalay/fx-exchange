package dto.request;

import java.math.BigDecimal;

public class DepositCreateDTO {

    private String currencyCode;
    private BigDecimal amount;
    private String method;

    public DepositCreateDTO() {}

    public DepositCreateDTO(String currencyCode, BigDecimal amount, String method) {
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.method = method;
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
    
}
