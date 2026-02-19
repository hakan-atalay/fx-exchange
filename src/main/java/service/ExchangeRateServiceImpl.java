package service;

import dao.ExchangeRateDAO;
import entity.ExchangeRate;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class ExchangeRateServiceImpl {

    @Inject
    private ExchangeRateDAO exchangeRateDAO;

    public ExchangeRate getOrFetchRate(String baseCurrency, String targetCurrency, BigDecimal rate, String source) {
        if (baseCurrency == null || targetCurrency == null || baseCurrency.equalsIgnoreCase(targetCurrency))
            throw new ServiceException("Invalid currency pair");

        baseCurrency = baseCurrency.toUpperCase();
        targetCurrency = targetCurrency.toUpperCase();

        Optional<ExchangeRate> existing = exchangeRateDAO.findByBaseAndTarget(baseCurrency, targetCurrency);

        if (existing.isPresent()) {
            ExchangeRate ex = existing.get();
            if (rate != null) {
                ex.setRate(rate);
                ex.setSource(source);
                ex.setFetchedAt(LocalDateTime.now());
                exchangeRateDAO.update(ex);
            }
            return ex;
        }

        ExchangeRate newRate = new ExchangeRate();
        newRate.setBaseCurrencyCode(baseCurrency);
        newRate.setTargetCurrencyCode(targetCurrency);
        newRate.setRate(rate);
        newRate.setSource(source);
        newRate.setFetchedAt(LocalDateTime.now());

        return exchangeRateDAO.save(newRate);
    }
}
