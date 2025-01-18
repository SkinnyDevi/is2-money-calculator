package apps.swing;

import model.Currency;
import model.Money;
import view.CurrencyDialog;
import view.MoneyDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class SwingMoneyDialog extends JPanel implements MoneyDialog {
    private JTextField amountTextField;
    private CurrencyDialog currencyDialog;
    private AmountUpdateEvent updateEvent = AmountUpdateEvent.Null;

    @Override
    public Money get() {
        return new Money(
                toDouble(amountTextField.getText()),
                currencyDialog.get()
        );
    }

    private double toDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public MoneyDialog setup(List<Currency> currencies) {
        add(createAmountTextField());
        add(BorderLayout.CENTER, createCurrencyDialog(currencies));
        return this;
    }

    @Override
    public void swapTo(Currency currency) {
        this.currencyDialog.swapTo(currency);
    }

    @Override
    public void on(AmountUpdateEvent event) {
        this.updateEvent = updateEvent != null ? event : AmountUpdateEvent.Null;
    }

    private Component createCurrencyDialog(List<Currency> currencies) {
        SwingCurrencyDialog dialog = new SwingCurrencyDialog();
        dialog.setup(currencies);
        dialog.on(this::notifyOfEvent);
        this.currencyDialog = dialog;
        return dialog;
    }

    private Component createAmountTextField() {
        JTextField textField = new JTextField("00.00");
        textField.setColumns(15);
        createDocumentListener(textField);
        this.amountTextField = textField;
        return textField;
    }

    private void createDocumentListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                notifyOfEvent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                notifyOfEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                notifyOfEvent();
            }
        });
    }

    private void notifyOfEvent() {
        this.updateEvent.notifyUpdate();
    }
}