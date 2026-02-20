package scheduler;

import dto.request.ExcangeRateCreateDTO;
import exception.SchedulerException;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.w3c.dom.*;
import service.ExchangeRateServiceImpl;

import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.net.URL;

@Singleton
@Startup
public class TcmbRateScheduler {

    @Inject
    private ExchangeRateServiceImpl exchangeRateService;

    private static final String TCMB_API_URL =
            System.getenv("TCMB_API_URL");

    @Schedule(hour = "*", minute = "0", persistent = false)
    public void fetchRates() {

        if (TCMB_API_URL == null || TCMB_API_URL.isBlank()) {
            throw new SchedulerException("TCMB_API_URL not configured");
        }

        try {
            URL url = new URL(TCMB_API_URL);

            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(url.openStream());

            NodeList currencies = doc.getElementsByTagName("Currency");

            for (int i = 0; i < currencies.getLength(); i++) {
                Element currency = (Element) currencies.item(i);

                String code = currency.getAttribute("CurrencyCode");
                String forexBuying = getTagValue(currency, "ForexBuying");

                if (forexBuying == null || forexBuying.isBlank())
                    continue;

                BigDecimal rate = new BigDecimal(forexBuying.replace(",", "."));

                ExcangeRateCreateDTO dto = new ExcangeRateCreateDTO();
                dto.setBaseCurrency("TRY");
                dto.setTargetCurrency(code);
                dto.setRate(rate);
                dto.setSource("TCMB");

                exchangeRateService.createOrUpdateRate(dto);
            }

        } catch (Exception e) {
            throw new SchedulerException("Failed to fetch TCMB rates", e);
        }
    }

    private String getTagValue(Element element, String tag) {
        NodeList list = element.getElementsByTagName(tag);
        if (list.getLength() == 0) return null;
        return list.item(0).getTextContent();
    }
}