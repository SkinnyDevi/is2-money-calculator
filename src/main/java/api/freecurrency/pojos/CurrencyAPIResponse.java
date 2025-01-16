package api.freecurrency.pojos;

public record CurrencyAPIResponse(
        String symbol,
        String name,
        String symbol_native,
        int decimal_digits,
        int rounding,
        String code,
        String name_plural
) {}