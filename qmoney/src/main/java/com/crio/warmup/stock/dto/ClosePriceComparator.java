package com.crio.warmup.stock.dto;
import java.util.Comparator;

//Comparator to sort the TiingoCandle objects on the closing prices
public class ClosePriceComparator implements Comparator<TiingoAndSymbol>{

  @Override
  public int compare(TiingoAndSymbol arg0, TiingoAndSymbol arg1) {
    
    return Double.compare(arg0.getTiingoCandle().getClose(), arg1.getTiingoCandle().getClose());
  }

  } 