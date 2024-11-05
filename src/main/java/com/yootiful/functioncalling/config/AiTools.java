package com.yootiful.functioncalling.config;

import com.yootiful.functioncalling.model.CryptoSymbol;
import com.yootiful.functioncalling.service.QuotationFunction;
import com.yootiful.functioncalling.service.QuotationService;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class AiTools {
  @Bean
    public FunctionCallback quotationFunction(@Autowired QuotationService quotationService) {
       return FunctionCallbackWrapper.builder(new QuotationFunction(quotationService))
               .withName("CurrentQuotation")
               .withDescription("""
                       Get the current quotation of the cryptocurrency by its symbol.
                       Available symbols are: %s
                       """.formatted(
                       Stream.of(CryptoSymbol.values()).map(CryptoSymbol::name)
                               .collect(Collectors.joining(", "))
               ))
               .build();
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
