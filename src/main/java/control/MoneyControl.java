package control;

import api.CurrencyAPI;
import model.Currency;
import model.ExchangeRate;
import model.Money;
import view.CurrencyDialog;
import view.MoneyDialog;
import view.MoneyDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyControl {
    private final MoneyDisplay moneyDisplay;
    private final MoneyDialog moneyDialog;
    private final CurrencyDialog currencyDialog;
    private final CurrencyAPI api;
    private final Map<Currency, List<ExchangeRate>> exchangeRateCache = new HashMap<>();

    public MoneyControl(
            MoneyDisplay moneyDisplay,
            MoneyDialog moneyDialog,
            CurrencyDialog currencyDialog,
            CurrencyAPI api
    ) {
        this.moneyDisplay = moneyDisplay;
        this.moneyDialog = moneyDialog;
        this.currencyDialog = currencyDialog;
        this.api = api;

        this.moneyDialog.on(this::updateDisplay);
        this.currencyDialog.on(this::updateDisplay);
    }

    private void updateDisplay() {
        Money result = getExchangedMoneyFromCurrentValues();
        if (result == null) {
            moneyDisplay.show(moneyDialog.get());
            return;
        }

        moneyDisplay.show(result);
    }

    private Money getExchangedMoneyFromCurrentValues() {
        Money money = moneyDialog.get();
        Currency exchangeCurrency = currencyDialog.get();
        if (money.currency().equals(exchangeCurrency)) {
            return null;
        }

        ExchangeRate exchangeRate = getExchangeRateFromCacheFor(money.currency(), exchangeCurrency);
        if (exchangeRate.equals(ExchangeRate.NULL))
            throwNoExchangeRateFoundException(money, exchangeCurrency);

        return calculateExchangeValue(money, exchangeRate, exchangeCurrency);
    }

    private static Money calculateExchangeValue(Money base, ExchangeRate exchangeRate, Currency exchangeCurrency) {
        return new Money(
                base.amount() * exchangeRate.rate(), exchangeCurrency
        );
    }

    private static void throwNoExchangeRateFoundException(Money base, Currency exchangeCurrency) {
        throw new RuntimeException(
                "Could not find exchange rate for base '" + base.currency().code() + "' and target '"
                + exchangeCurrency.code() + "'"
        );
    }

    private ExchangeRate getExchangeRateFromCacheFor(Currency currency, Currency exchangeCurrency) {
        return getExchangeRatesFromCacheFor(currency)
                .stream().filter(er -> er.to().equals(exchangeCurrency))
                .findFirst().orElse(ExchangeRate.NULL);
    }

    private List<ExchangeRate> getExchangeRatesFromCacheFor(Currency currency) {
        return exchangeRateCache
                .computeIfAbsent(currency, c -> api.getExchangeRatesFor(c.code()));
    }

    public void swapDialogContents() {
        Money currentMoney = moneyDialog.get();
        Money convertedMoney = getExchangedMoneyFromCurrentValues();
        if (convertedMoney == null)
            return;

        moneyDialog.swapTo(convertedMoney.currency());
        currencyDialog.swapTo(currentMoney.currency());
        this.updateDisplay();
    }
}