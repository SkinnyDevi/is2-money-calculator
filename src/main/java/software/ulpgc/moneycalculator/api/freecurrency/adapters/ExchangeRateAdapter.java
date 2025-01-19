package software.ulpgc.moneycalculator.api.freecurrency.adapters;

import software.ulpgc.moneycalculator.api.freecurrency.pojos.ExchangeRateAPIResponse;
import software.ulpgc.moneycalculator.model.Currency;
import software.ulpgc.moneycalculator.model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public class ExchangeRateAdapter {
    private final List<Currency> currencies;
    private final Currency base;

    public ExchangeRateAdapter(List<Currency> currencies, Currency base) {
        this.currencies = currencies;
        this.base = base;
    }

    public ExchangeRate adapt(ExchangeRateAPIResponse response) {
        return new ExchangeRate(
                base,
                findInCurrencies(response),
                LocalDate.now(),
                response.rate()
        );
    }

    private Currency findInCurrencies(ExchangeRateAPIResponse response) {
        try {
            return currencies.stream()
                    .filter(c -> response.code().equals(c.code()))
                    .findFirst().orElseThrow();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }
}