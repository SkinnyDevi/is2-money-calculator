package api.mock;

import api.CurrencyAPI;
import model.Currency;
import model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;

public class MockAPI implements CurrencyAPI {
    @Override
    public List<Currency> getCurrencies() {
        return List.of(
                new Currency("$", "US Dollar", "USD"),
                new Currency("€", "Euro", "EUR"),
                new Currency("¥", "Japanese Yen", "JPY")
        );
    }

    @Override
    public Currency getCurrencyFor(String code) {
        return getCurrencies().stream().filter(c -> c.code().equals(code)).findFirst().orElse(Currency.NULL);
    }

    @Override
    public List<Currency> getCurrenciesFor(List<String> codes) {
        return getCurrencies().stream().filter(c -> codes.contains(c.code())).toList();
    }

    @Override
    public List<ExchangeRate> getExchangeRates() {
        List<Currency> currencies = getCurrencies();
        return List.of(
                new ExchangeRate(currencies.get(0), currencies.get(1), LocalDate.now(), 1.032334),
                new ExchangeRate(currencies.get(0), currencies.get(2), LocalDate.now(), 0.0064871)
        );
    }

    @Override
    public List<ExchangeRate> getExchangeRatesFor(String baseCode) {
        Currency base = getCurrencyFor(baseCode);
        List<Currency> currencies = getCurrencies().stream().filter(c -> c != base).toList();
        return List.of(
                new ExchangeRate(base, currencies.get(0), LocalDate.now(), 1.123541),
                new ExchangeRate(base, currencies.get(1), LocalDate.now(), 0.231412)
        );
    }

    @Override
    public List<ExchangeRate> getExchangeRatesFor(String baseCode, List<String> codes) {
        Currency base = getCurrencyFor(baseCode);
        List<Currency> currencies = getCurrenciesFor(codes).stream().filter(c -> c != base).toList();
        return List.of(
                new ExchangeRate(base, currencies.get(0), LocalDate.now(), 1.41251),
                new ExchangeRate(base, currencies.get(1), LocalDate.now(), 0.124412)
        );
    }
}