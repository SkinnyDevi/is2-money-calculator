package software.ulpgc.moneycalculator.api.freecurrency.adapters;

import software.ulpgc.moneycalculator.api.freecurrency.pojos.CurrencyAPIResponse;
import software.ulpgc.moneycalculator.model.Currency;

public class CurrencyAdapter {
    public static Currency adapt(CurrencyAPIResponse response) {
        return new Currency(
                response.symbol(),
                response.name(),
                response.code()
        );
    }
}