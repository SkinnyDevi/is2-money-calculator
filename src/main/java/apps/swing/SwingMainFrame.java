package apps.swing;

import control.Command;
import control.CommandIdentifiers;
import model.Currency;
import view.CurrencyDialog;
import view.MoneyDialog;
import view.MoneyDisplay;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingMainFrame extends JFrame {
    public static final int HORIZONTAL_MARGIN = 10;
    private final Map<String, Command> commands = new HashMap<>();
    private MoneyDialog moneyDialog;
    private MoneyDisplay moneyDisplay;
    private CurrencyDialog currencyDialog;

    public SwingMainFrame(List<Currency> currencies) throws HeadlessException {
        this.setTitle("Money Calculator");
        this.setSize(600, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(600, 300));
        add(createMainContainer(currencies));
    }

    private Component createMainContainer(List<Currency> currencies) {
        JPanel mainContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainContainer.add(createMoneyDialog(currencies));

        gbc.gridx = 1;
        mainContainer.add(createHelperButtons());

        gbc.gridx = 2;
        mainContainer.add(createMoneyDisplayContainer(currencies));

        return mainContainer;
    }

    private Component createMoneyDisplayContainer(List<Currency> currencies) {
        JPanel jPanel = new JPanel(createGridLayout());
        jPanel.setBorder(createMarginSpacingBorder());
        jPanel.add(createMoneyDisplay());
        jPanel.add(BorderLayout.CENTER, createCurrencyDialog(currencies));
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

    private Component createHelperButtons() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(createMarginSpacingBorder());
        jPanel.add(createSwapButton());
        return jPanel;
    }

    private void configureButton(JButton jButton, CommandIdentifiers identifier) {
        styleButton(jButton);
        addActionListenerTo(jButton, identifier);
    }

    private void addActionListenerTo(JButton jButton, CommandIdentifiers identifier) {
        jButton.addActionListener(__ -> executeCommandFor(identifier));
    }

    private void styleButton(JButton jButton) {

    }

    private Component createSwapButton() {
        JButton jButton = new JButton("Swap");
        configureButton(jButton, CommandIdentifiers.SWAP_DIALOG_CONTENTS);
        return jButton;
    }

    private Component createMoneyDialog(List<Currency> currencies) {
        SwingMoneyDialog swingMoneyDialog = new SwingMoneyDialog();
        swingMoneyDialog.setup(currencies);
        swingMoneyDialog.setBorder(createMarginSpacingBorder());
        swingMoneyDialog.setLayout(createGridLayout());
        this.moneyDialog = swingMoneyDialog;
        return swingMoneyDialog;
    }

    private LayoutManager createGridLayout() {
        return new GridLayout(2, 1, 0, 10);
    }

    private static Border createMarginSpacingBorder() {
        return BorderFactory.createEmptyBorder(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_MARGIN);
    }

    public SwingMainFrame put(CommandIdentifiers identifier, Command command) {
        this.commands.put(identifier.get(), command);
        return this;
    }

    private void executeCommandFor(CommandIdentifiers identifier) {
        commands.get(identifier.get()).execute();
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