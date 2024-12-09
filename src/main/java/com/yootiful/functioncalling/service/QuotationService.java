package com.yootiful.functioncalling.service;

import com.yootiful.functioncalling.entity.Stock;
import com.yootiful.functioncalling.model.Quotation;
import com.yootiful.functioncalling.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class QuotationService {
    private final RestTemplate restTemplate;
    private final StockRepository stockRepository;
    public QuotationService(RestTemplate restTemplate, StockRepository stockRepository) {
        this.restTemplate = restTemplate;
        this.stockRepository = stockRepository;
    }

    public Quotation fetch(String cryptoSymbol) {
        System.out.println("2. Function call initiated for " + cryptoSymbol);
        Map<String, Object> responseMap = null;

        try {
            responseMap = restTemplate.getForObject(
                    "https://api.diadata.org/v1/quotation/" + cryptoSymbol, Map.class
            );
        } catch (Exception e) {

        }

        if (responseMap == null) {
            return new Quotation(null, null, 0.0, 0.0, 0.0);
        }

        Optional<Stock> stock = stockRepository.findBySymbol((String) responseMap.get("Symbol"));

        Map<String, Object> finalResponseMap = responseMap;
        stock.ifPresent(existingStock -> {
            Double price = (Double) finalResponseMap.get("Price");
            System.out.println("3. The price is " + price + " against the minimum price of " + existingStock.getMinPrice());
            if (price > existingStock.getMinPrice().doubleValue()) {
                System.out.println("4. Alert is ON. We must SELL");
                existingStock.setAlert(true);
                existingStock.setDescription("SELL");
            } else {
                System.out.println("4. Alert is OFF. We must BUY");
                existingStock.setAlert(false);
                existingStock.setDescription("BUY");
            }

            stockRepository.save(existingStock);
        });

        return new Quotation(
                (String) responseMap.get("Symbol"),
                (String) responseMap.get("Name"),
                (Double) responseMap.get("Price"),
                (Double) responseMap.get("PriceYesterday"),
                (Double) responseMap.get("VolumeYesterdayUSD")
        );
    }
}
