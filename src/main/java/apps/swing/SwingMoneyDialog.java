package apps.swing;

import model.Currency;
import model.Money;
import view.AppColors;
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
        this.setBackground(getColor(AppColors.PANEL_BACKGROUND_COLOR));
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
        dialog.setBackground(getColor(AppColors.PANEL_BACKGROUND_COLOR));
        this.currencyDialog = dialog;
        return dialog;
    }

    private Color getColor(AppColors color) {
        return color.get();
    }

    private Component createAmountTextField() {
        JTextField textField = new JTextField("00.00");
        setupTextField(textField);
        this.amountTextField = textField;
        return textField;
    }

    private void setupTextField(JTextField textField) {
        textField.setColumns(15);
        textField.setCaretColor(getColor(AppColors.LABEL_TEXT_COLOR));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBackground(getColor(AppColors.PANEL_BACKGROUND_COLOR));
        textField.setForeground(getColor(AppColors.LABEL_TEXT_COLOR));
        createDocumentListener(textField);
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