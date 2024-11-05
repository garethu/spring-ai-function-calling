package com.yootiful.functioncalling.model;

import lombok.Getter;

@Getter
public enum CryptoSymbol {
    BTC("Bitcoin"), ETH("Ethereum"), SOL("Solana");

    private final String title;

    CryptoSymbol(String title) {
        this.title = title;
    }
}

