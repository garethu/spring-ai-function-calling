package com.yootiful.functioncalling.service;

import com.yootiful.functioncalling.model.Quotation;

import java.util.function.Function;
public class QuotationFunction implements Function<QuotationFunction.Request, Quotation> {

    private final QuotationService quotationService;

    public QuotationFunction(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    public record Request(String cryptoSymbol) {
    }

    @Override
    public Quotation apply(Request request) {
        return quotationService.fetch(request.cryptoSymbol);
    }
}
