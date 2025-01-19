package software.ulpgc.moneycalculator.view;

import software.ulpgc.moneycalculator.model.Currency;
import software.ulpgc.moneycalculator.model.Money;

import java.util.List;

public interface MoneyDialog {
	Money get();

	MoneyDialog setup(List<Currency> currencies);

	void swapTo(Currency currency);

	void on(AmountUpdateEvent event);

	interface AmountUpdateEvent {
		AmountUpdateEvent Null = () -> {
		};

		void notifyUpdate();
	}
}