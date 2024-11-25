package com.yootiful.functioncalling.config;

import com.yootiful.functioncalling.model.Quotation;
import com.yootiful.functioncalling.service.QuotationFunction;
import com.yootiful.functioncalling.service.QuotationService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.function.Function;

@Configuration
public class BeanProvider {
    @Bean
    @Description("Provides the current quotation of a cryptocurrency by symbol")
    public Function<QuotationFunction.Request, Quotation> getQuotation(QuotationService quotationService) {
        return request -> {
            try {
              return quotationService.fetch(request.symbol());
            }
            catch (Exception e) {
               return new Quotation(null,null,0.0,0.0,0.0);
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        ignoreCertificates();
        return builder.build();
    }

    private void ignoreCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }
}
