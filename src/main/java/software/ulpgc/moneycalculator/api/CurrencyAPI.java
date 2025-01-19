package software.ulpgc.moneycalculator.api;

import software.ulpgc.moneycalculator.model.ExchangeRate;

import software.ulpgc.moneycalculator.model.Currency;
import java.util.List;

public interface CurrencyAPI {
    List<Currency> getCurrencies();
    Currency getCurrencyFor(String code);
    List<Currency> getCurrenciesFor(List<String> codes);
    List<ExchangeRate> getExchangeRates();
    List<ExchangeRate> getExchangeRatesFor(String baseCode);
    ExchangeRate getExchangeRateFor(String baseCode, String code);
    List<ExchangeRate> getExchangeRatesFor(String baseCode, List<String> codes);
}