
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {



  private StockQuotesService stockQuotesService;
  private RestTemplate restTemplate;
  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  public PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  public PortfolioManagerImpl(StockQuotesService stockQuotesService){
    this.stockQuotesService = stockQuotesService;
  }

  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF






  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
        return this.stockQuotesService.getStockQuote(symbol, from, to);
  }
  private static String getToken() {
    return "dc34e3a14fe19c1c2244dfb51f6e93f90b360aaa";
  }
  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String token = getToken();
       String apiUrl = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s",symbol,startDate,endDate,token);
       return apiUrl;
  }
  public static AnnualizedReturn calculateAnnualizedReturnsForASymbol(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        Double totalReturn = (sellPrice-buyPrice)/buyPrice;
        Double daysBetween = (double)ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
        Double totalNumYears = daysBetween/(double)365;
        Double annualizedReturns = Math.pow((1+totalReturn),(1/totalNumYears))- 1;
      return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturn);
  }
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
    return candles.get(0).getOpen();
 }


 public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    return candles.get(candles.size()-1).getClose();
 }
  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> trades, LocalDate endDate)
      throws JsonProcessingException {
    List<AnnualizedReturn> annualizedReturn =  new ArrayList<AnnualizedReturn>();
    for(PortfolioTrade trade: trades){
        List<Candle> fetchedCandles = getStockQuote(trade.getSymbol(), trade.getPurchaseDate(), endDate);
        annualizedReturn.add(calculateAnnualizedReturnsForASymbol(fetchedCandles.get(fetchedCandles.size()-1).getDate(), trade, getOpeningPriceOnStartDate(fetchedCandles), getClosingPriceOnEndDate(fetchedCandles)));
    }
    List<AnnualizedReturn> sortedAnnualizedReturn = annualizedReturn.stream()
                                                    .sorted((p1,p2)->-(p1.getAnnualizedReturn().compareTo(p2.getAnnualizedReturn())))
                                                    .collect(Collectors.toList());
    return sortedAnnualizedReturn;
  }


  // ¶TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Modify the function #getStockQuote and start delegating to calls to
  //  stockQuoteService provided via newly added constructor of the class.
  //  You also have a liberty to completely get rid of that function itself, however, make sure
  //  that you do not delete the #getStockQuote function.

}
