package view;

import model.Currency;

import java.util.List;

public interface CurrencyDialog {
    Currency get();
    CurrencyDialog setup(List<Currency> currencies);
    void swapTo(Currency currency);
    void on(CurrencyUpdateEvent event);

    interface CurrencyUpdateEvent {
        CurrencyUpdateEvent Null = () -> {};
        void notifyUpdate();
    }
}