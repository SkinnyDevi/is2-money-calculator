package software.ulpgc.moneycalculator.control;

import software.ulpgc.moneycalculator.api.CurrencyAPI;
import software.ulpgc.moneycalculator.model.Currency;
import software.ulpgc.moneycalculator.model.ExchangeRate;
import software.ulpgc.moneycalculator.model.Money;
import software.ulpgc.moneycalculator.view.CurrencyDialog;
import software.ulpgc.moneycalculator.view.ExchangeRateDisplay;
import software.ulpgc.moneycalculator.view.MoneyDialog;
import software.ulpgc.moneycalculator.view.MoneyDisplay;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyPresenter {
	private final MoneyDisplay moneyDisplay;
	private final MoneyDialog moneyDialog;
	private final CurrencyDialog currencyDialog;
	private final CurrencyAPI api;
	private final ExchangeRateDisplay exchangeRateDisplay;
	private final Map<Currency, List<ExchangeRate>> exchangeRateCache = new HashMap<>();

	public MoneyPresenter(
			MoneyDisplay moneyDisplay,
			MoneyDialog moneyDialog,
			CurrencyDialog currencyDialog,
			CurrencyAPI api,
			ExchangeRateDisplay exchangeRateDisplay) {
		this.moneyDisplay = moneyDisplay;
		this.moneyDialog = moneyDialog;
		this.currencyDialog = currencyDialog;
		this.api = api;
		this.exchangeRateDisplay = exchangeRateDisplay;

		this.moneyDialog.on(this::updateDisplay);
		this.currencyDialog.on(this::updateDisplay);
	}

	private void updateDisplay() {
		Money result = getExchangedMoneyFromCurrentValues();
		if (result == null) {
			moneyDisplay.show(moneyDialog.get());
			exchangeRateDisplay.show(getEqualExchangeRateFor(moneyDialog.get()));
			return;
		}

		exchangeRateDisplay.show(getCurrentExchangeRate());
		moneyDisplay.show(result);
	}

	private ExchangeRate getEqualExchangeRateFor(Money money) {
		return new ExchangeRate(money.currency(), money.currency(), LocalDate.now(), 1.0);
	}

	private Money getExchangedMoneyFromCurrentValues() {
		Money money = moneyDialog.get();
		Currency exchangeCurrency = currencyDialog.get();
		if (money.currency().equals(exchangeCurrency)) {
			return null;
		}

		ExchangeRate exchangeRate = getCurrentExchangeRate(money, exchangeCurrency);
		return calculateExchangeValue(money, exchangeRate, exchangeCurrency);
	}

	private ExchangeRate getCurrentExchangeRate() {
		return getCurrentExchangeRate(moneyDialog.get(), currencyDialog.get());
	}

	private ExchangeRate getCurrentExchangeRate(Money money, Currency exchangeCurrency) {
		ExchangeRate exchangeRate = getExchangeRateFromCacheFor(money.currency(), exchangeCurrency);
		if (exchangeRate.equals(ExchangeRate.NULL))
			throwNoExchangeRateFoundException(money, exchangeCurrency);
		return exchangeRate;
	}

	private static Money calculateExchangeValue(Money base, ExchangeRate exchangeRate, Currency exchangeCurrency) {
		return new Money(
				base.amount() * exchangeRate.rate(), exchangeCurrency);
	}

	private static void throwNoExchangeRateFoundException(Money base, Currency exchangeCurrency) {
		throw new RuntimeException(
				"Could not find exchange rate for base '" + base.currency().code() + "' and target '"
						+ exchangeCurrency.code() + "'");
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