package api.freecurrency.adapters;

import api.freecurrency.pojos.CurrencyAPIResponse;
import model.Currency;

public class CurrencyAdapter {
    public static Currency adapt(CurrencyAPIResponse response) {
        return new Currency(
                response.symbol(),
                response.name(),
                response.code()
        );
    }
}