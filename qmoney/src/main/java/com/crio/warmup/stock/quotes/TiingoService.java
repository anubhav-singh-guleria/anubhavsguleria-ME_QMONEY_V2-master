
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  
  private final RestTemplate restTemplate;

  protected TiingoService(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  private static String getToken() {
    return "dc34e3a14fe19c1c2244dfb51f6e93f90b360aaa";
  }

  protected String buildUri(final String symbol, final LocalDate startDate, final LocalDate endDate) {
    final String token = getToken();
    final String apiUrl = String.format(
        "https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s", symbol, startDate, endDate,
        token);
    return apiUrl;
  }

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws JsonProcessingException {
    // TODO Auto-generated method stub
    String url = buildUri(symbol, from, to);
    if(from.compareTo(to)>=0) throw new RuntimeException();
    List<Candle> stocksList;
    String stocks = restTemplate.getForObject(url, String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    TiingoCandle[] stocksListArray = objectMapper.readValue(stocks, TiingoCandle[].class);
    if(stocksListArray!=null)stocksList = Arrays.asList(stocksListArray);
    else stocksList = Arrays.asList(new TiingoCandle[0]);
    return stocksList;
  }

  


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

}
