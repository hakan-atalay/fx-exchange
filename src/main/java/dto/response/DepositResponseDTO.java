package dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DepositResponseDTO {

	private Long id;
	private String currencyCode;
	private BigDecimal amount;
	private String method;
	private String status;
	private LocalDateTime createdAt;

	public DepositResponseDTO() {
	}

	public DepositResponseDTO(Long id, String currencyCode, BigDecimal amount, String method, String status,
			LocalDateTime createdAt) {
		this.id = id;
		this.currencyCode = currencyCode;
		this.amount = amount;
		this.method = method;
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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
