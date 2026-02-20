package service;

import dao.ExchangeRateDAO;
import dto.request.ExcangeRateCreateDTO;
import dto.response.ExchangeRateResponseDTO;
import entity.ExchangeRate;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mapper.ExchangeRateMapper;
import service.interfaces.ExchangeRateService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class ExchangeRateServiceImpl implements ExchangeRateService{

	@Inject
	private ExchangeRateDAO exchangeRateDAO;

	public ExchangeRateResponseDTO createOrUpdateRate(ExcangeRateCreateDTO request) {
		validateRequest(request);

		String baseCurrency = request.getBaseCurrency().toUpperCase();
		String targetCurrency = request.getTargetCurrency().toUpperCase();

		Optional<ExchangeRate> existingRate = exchangeRateDAO.findByBaseAndTarget(baseCurrency, targetCurrency);

		ExchangeRate rateEntity = existingRate.map(rate -> {
			if (request.getRate() != null) {
				rate.setRate(request.getRate());
				rate.setSource(request.getSource());
				rate.setFetchedAt(LocalDateTime.now());
				exchangeRateDAO.update(rate);
			}
			return rate;
		}).orElseGet(() -> {
			ExchangeRate newRate = new ExchangeRate();
			newRate.setBaseCurrencyCode(baseCurrency);
			newRate.setTargetCurrencyCode(targetCurrency);
			newRate.setRate(request.getRate());
			newRate.setSource(request.getSource());
			newRate.setFetchedAt(LocalDateTime.now());
			return exchangeRateDAO.save(newRate);
		});

		return ExchangeRateMapper.toResponse(rateEntity);
	}

	private void validateRequest(ExcangeRateCreateDTO request) {
		if (request == null)
			throw new ServiceException("Exchange rate request is required");
		if (request.getBaseCurrency() == null || request.getTargetCurrency() == null
				|| request.getBaseCurrency().equalsIgnoreCase(request.getTargetCurrency()))
			throw new ServiceException("Invalid currency pair");
	}
	
	public BigDecimal getRate(String base, String target) {

	    if (base == null || target == null || base.equalsIgnoreCase(target))
	        throw new ServiceException("Invalid currency pair");

	    return exchangeRateDAO
	            .findByBaseAndTarget(base.toUpperCase(), target.toUpperCase())
	            .map(ExchangeRate::getRate)
	            .orElseThrow(() ->
	                    new ServiceException("Exchange rate not available"));
	}
}
