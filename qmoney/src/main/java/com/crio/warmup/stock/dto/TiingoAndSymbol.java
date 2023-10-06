package com.crio.warmup.stock.dto;

public class TiingoAndSymbol {
    private TiingoCandle tiingoCandle;
    private String symbol;

    public TiingoCandle getTiingoCandle() {
        return tiingoCandle;
    }

    public void setTiingoCandle(TiingoCandle tiingoCandle) {
        this.tiingoCandle = tiingoCandle;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}