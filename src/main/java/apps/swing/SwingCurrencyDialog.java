package apps.swing;

import model.Currency;
import view.CurrencyDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class SwingCurrencyDialog extends JPanel implements CurrencyDialog {
    private JComboBox<Currency> currencyDropdown;
    private CurrencyUpdateEvent updateEvent = CurrencyUpdateEvent.Null;
    private Currency previousSelection = Currency.NULL;

    @Override
    public Currency get() {
        return this.currencyDropdown.getItemAt(currencyDropdown.getSelectedIndex());
    }

    @Override
    public CurrencyDialog setup(List<Currency> currencies) {
        setLayout(new BorderLayout());
        add(createDropdownComponent(currencies));
        return this;
    }

    @Override
    public void swapTo(Currency currency) {
        currencyDropdown.setSelectedItem(currency);
    }

    @Override
    public void on(CurrencyUpdateEvent event) {
        this.updateEvent = event != null ? event : CurrencyUpdateEvent.Null;
    }

    private Component createDropdownComponent(List<Currency> currencies) {
        JComboBox<Currency> dropdown = new JComboBox<>();
        currencies.forEach(dropdown::addItem);
        dropdown.addActionListener(createActionListener(dropdown));
        this.currencyDropdown = dropdown;
        return dropdown;
    }

    private ActionListener createActionListener(JComboBox<Currency> dropdown) {
        return e -> {
            Currency selected = (Currency) dropdown.getSelectedItem();
            if (selected.equals(Currency.NULL) || !selected.equals(previousSelection)) {
                previousSelection = selected;
                updateEvent.notifyUpdate();
            }
        };
    }
}