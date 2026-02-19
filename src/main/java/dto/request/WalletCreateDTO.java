package dto.request;

public class WalletCreateDTO {
	private String currencyCode;

	public WalletCreateDTO() {
	}

	public WalletCreateDTO(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}
