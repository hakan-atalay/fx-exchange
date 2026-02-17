package dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WithdrawalResponseDTO {

	private Long id;
	private String currencyCode;
	private BigDecimal amount;
	private String iban;
	private String status;
	private LocalDateTime createdAt;

	public WithdrawalResponseDTO() {
	}

	public WithdrawalResponseDTO(Long id, String currencyCode, BigDecimal amount, String iban, String status,
			LocalDateTime createdAt) {
		this.id = id;
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
}
