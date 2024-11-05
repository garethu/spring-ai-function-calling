package com.yootiful.functioncalling.service;

import com.yootiful.functioncalling.model.Quotation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class QuotationService {
    private final RestTemplate restTemplate;

    public QuotationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Quotation fetch(String cryptoSymbol) {
        System.out.println("Fetching quotation for " + cryptoSymbol);
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

        return new Quotation(
                (String) responseMap.get("Symbol"),
                (String) responseMap.get("Name"),
                (Double) responseMap.get("Price"),
                (Double) responseMap.get("PriceYesterday"),
                (Double) responseMap.get("VolumeYesterdayUSD")
        );
    }
}
