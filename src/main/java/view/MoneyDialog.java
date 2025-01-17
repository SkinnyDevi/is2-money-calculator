package view;

import model.Currency;
import model.Money;

import java.util.List;

public interface MoneyDialog {
    Money get();
    MoneyDialog setup(List<Currency> currencies);
    void swapTo(Currency currency);
    void on(AmountUpdateEvent event);

    interface AmountUpdateEvent {
        AmountUpdateEvent Null = () -> {};
        void notifyUpdate();
    }
}