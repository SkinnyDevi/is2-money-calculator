package apps.swing;

import control.Command;
import control.CommandIdentifiers;
import model.Currency;
import view.CurrencyDialog;
import view.MoneyDialog;
import view.MoneyDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingMainFrame extends JFrame {
    private final Map<String, Command> commands = new HashMap<>();
    private MoneyDialog moneyDialog;
    private MoneyDisplay moneyDisplay;
    private CurrencyDialog currencyDialog;

    public SwingMainFrame(List<Currency> currencies) throws HeadlessException {
        this.setTitle("Money Calculator");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        add(BorderLayout.WEST, createMoneyDialog(currencies));
        add(BorderLayout.EAST, createMoneyDisplayContainer(currencies));
        add(BorderLayout.CENTER, createHelperButtons());
    }

    private Component createMoneyDisplayContainer(List<Currency> currencies) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.NORTH, createMoneyDisplay());
        jPanel.add(BorderLayout.SOUTH, createCurrencyDialog(currencies));
        return jPanel;
    }

    private Component createCurrencyDialog(List<Currency> currencies) {
        SwingCurrencyDialog swingCurrencyDialog = new SwingCurrencyDialog();
        swingCurrencyDialog.setup(currencies);
        this.currencyDialog = swingCurrencyDialog;
        return swingCurrencyDialog;
    }

    private Component createMoneyDisplay() {
        SwingMoneyDisplay display = new SwingMoneyDisplay();
        this.moneyDisplay = display;
        return display;
    }

    private Component createMoneyDialog(List<Currency> currencies) {
        SwingMoneyDialog swingMoneyDialog = new SwingMoneyDialog();
        swingMoneyDialog.setup(currencies);
        this.moneyDialog = swingMoneyDialog;
        return swingMoneyDialog;
    }

    public SwingMainFrame put(CommandIdentifiers identifier, Command command) {
        this.commands.put(identifier.get(), command);
        return this;
    }

    private Component createHelperButtons() {
        JPanel jPanel = new JPanel();
        jPanel.add(createSwapButton());
        return jPanel;
    }

    private Component createSwapButton() {
        JButton jButton = new JButton("Swap");
        configureButton(jButton, CommandIdentifiers.SWAP_DIALOG_CONTENTS);
        return jButton;
    }

    private void configureButton(JButton jButton, CommandIdentifiers identifier) {
        styleButton(jButton);
        addActionListenerTo(jButton, identifier);
    }

    private void addActionListenerTo(JButton jButton, CommandIdentifiers identifier) {
        jButton.addActionListener(__ -> executeCommandFor(identifier));
    }

    private void executeCommandFor(CommandIdentifiers identifier) {
        commands.get(identifier.get()).execute();
    }

    private void styleButton(JButton jButton) {

    }

    public MoneyDialog getMoneyDialog() {
        return moneyDialog;
    }

    public MoneyDisplay getMoneyDisplay() {
        return moneyDisplay;
    }

    public CurrencyDialog getCurrencyDialog() {
        return currencyDialog;
    }
}