package mapper;

import dto.response.ExchangeRateResponseDTO;
import entity.ExchangeRate;

public class ExchangeRateMapper {

	public static ExchangeRateResponseDTO toResponse(ExchangeRate entity) {
		if (entity == null)
			return null;

		ExchangeRateResponseDTO dto = new ExchangeRateResponseDTO();
		dto.setBaseCurrency(entity.getBaseCurrencyCode());
		dto.setTargetCurrency(entity.getTargetCurrencyCode());
		dto.setRate(entity.getRate());
		dto.setSource(entity.getSource());
		dto.setFetchedAt(entity.getFetchedAt());
		return dto;
	}
}
