package dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExchangeRateResponseDTO {
	private String baseCurrency;
	private String targetCurrency;
	private BigDecimal rate;
	private String source;
	private LocalDateTime fetchedAt;

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
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

}
