package dto.response;

import java.math.BigDecimal;

public class WalletResponseDTO {

    private Long id;
    private String currencyCode;
    private BigDecimal balance;

    public WalletResponseDTO() {}

    public WalletResponseDTO(Long id, String currencyCode, BigDecimal balance) {
        this.id = id;
        this.currencyCode = currencyCode;
        this.balance = balance;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

   
}
