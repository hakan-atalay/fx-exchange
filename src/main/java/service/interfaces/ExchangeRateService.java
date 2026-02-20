package service.interfaces;

import java.math.BigDecimal;

import dto.request.ExcangeRateCreateDTO;
import dto.response.ExchangeRateResponseDTO;

public interface ExchangeRateService {

	ExchangeRateResponseDTO createOrUpdateRate(ExcangeRateCreateDTO request);

	BigDecimal getRate(String base, String target);
}