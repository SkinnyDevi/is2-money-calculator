package software.ulpgc.moneycalculator.apps.swing;

import software.ulpgc.moneycalculator.control.Command;
import software.ulpgc.moneycalculator.control.CommandIdentifiers;
import software.ulpgc.moneycalculator.model.Currency;
import software.ulpgc.moneycalculator.view.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingMainFrame extends JFrame {
    public static final int HORIZONTAL_MARGIN = 10;
    public static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    public static final Dimension WINDOW_DIMENSIONS = new Dimension(700, 200);
    private final Map<String, Command> commands = new HashMap<>();
    private MoneyDialog moneyDialog;
    private MoneyDisplay moneyDisplay;
    private CurrencyDialog currencyDialog;
    private ExchangeRateDisplay exchangeRateDisplay;

    public SwingMainFrame(List<Currency> currencies) throws HeadlessException {
        this.setTitle("Currency Converter");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setBackground(getColor(AppColors.PANEL_BACKGROUND_COLOR));
        lockWindowSize();

        this.setLayout(new BorderLayout());
        this.add(createContentContainer(currencies), BorderLayout.CENTER);
    }

    private void lockWindowSize() {
        this.setResizable(false);
        this.setSize(WINDOW_DIMENSIONS);
    }

    private Component createContentContainer(List<Currency> currencies) {
        JPanel jPanel = createPanelWithBackgroundAppColor();
        jPanel.add(createTitleContainer());
        jPanel.add(createMainContainer(currencies));
        return jPanel;
    }

    private Component createMainContainer(List<Currency> currencies) {
        JPanel mainContainer = createPanelWithBackgroundAppColor();
        mainContainer.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainContainer.add(createMoneyDialog(currencies));

        gbc.gridx = 1;
        mainContainer.add(createHelperButtons());

        gbc.gridx = 2;
        mainContainer.add(createMoneyDisplayContainer(currencies));

        return mainContainer;
    }

    private Component createTitleContainer() {
        JPanel jPanel = createPanelWithBackgroundAppColor();
        jPanel.add(createTitleText());
        return jPanel;
    }

    private Component createTitleText() {
        JLabel label = new JLabel("Money Calculator");
        label.setFont(TITLE_FONT);
        label.setForeground(getColor(AppColors.LABEL_TEXT_COLOR));
        label.setBackground(getColor(AppColors.BACKGROUND_BUTTON_HOVER_COLOR));
        return label;
    }

    private Component createMoneyDisplayContainer(List<Currency> currencies) {
        JPanel jPanel = createPanelWithBackgroundAppColor();
        jPanel.setLayout(createGridLayoutWithVerticalSpacing());
        jPanel.setBorder(createMarginSpacingBorder());
        jPanel.add(createMoneyDisplay());
        jPanel.add(BorderLayout.CENTER, createCurrencyDialog(currencies));
        return jPanel;
    }

    private Component createCurrencyDialog(List<Currency> currencies) {
        SwingCurrencyDialog swingCurrencyDialog = new SwingCurrencyDialog();
        swingCurrencyDialog.setup(currencies);
        swingCurrencyDialog.setBackground(getColor(AppColors.PANEL_BACKGROUND_COLOR));
        this.currencyDialog = swingCurrencyDialog;
        return swingCurrencyDialog;
    }

    private Component createMoneyDisplay() {
        SwingMoneyDisplay display = new SwingMoneyDisplay();
        this.moneyDisplay = display;
        return display;
    }

    private Component createHelperButtons() {
        JPanel jPanel = createPanelWithBackgroundAppColor();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(createMarginSpacingBorder());
        jPanel.add(createSwapButton(), BorderLayout.NORTH);
        jPanel.add(createVerticalSeparatorWith(20));
        jPanel.add(createExchangeRateDisplay(), BorderLayout.SOUTH);
        return jPanel;
    }

    private static Component createVerticalSeparatorWith(int height) {
        return Box.createRigidArea(new Dimension(0, height));
    }

    private Component createExchangeRateDisplay() {
        SwingExchangeRateDisplay swingExchangeRateDisplay = new SwingExchangeRateDisplay();
        swingExchangeRateDisplay.setFont(TEXT_FONT);
        this.exchangeRateDisplay = swingExchangeRateDisplay;
        return swingExchangeRateDisplay;
    }

    private JPanel createPanelWithBackgroundAppColor() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(getColor(AppColors.PANEL_BACKGROUND_COLOR));
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

    private void styleButton(JButton jButton) {
        jButton.setOpaque(true);
        jButton.setPreferredSize(new Dimension(140, 30));
        jButton.setFont(TEXT_FONT);
        jButton.setForeground(Color.WHITE);
        jButton.setBackground(getColor(AppColors.BUTTON_BG_COLOR));
        jButton.setFocusPainted(false);
        jButton.setBorder(createRoundedBorder());
    }

    private Border createRoundedBorder() {
        return SwingRoundedBorder.with(20);
    }

    private Color getColor(AppColors color) {
        return color.get();
    }

    private Component createMoneyDialog(List<Currency> currencies) {
        SwingMoneyDialog swingMoneyDialog = new SwingMoneyDialog();
        swingMoneyDialog.setup(currencies);
        swingMoneyDialog.setBorder(createMarginSpacingBorder());
        swingMoneyDialog.setLayout(createGridLayoutWithVerticalSpacing());
        this.moneyDialog = swingMoneyDialog;
        return swingMoneyDialog;
    }

    private LayoutManager createGridLayoutWithVerticalSpacing() {
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

    public ExchangeRateDisplay getExchangeRateDisplay() {
        return exchangeRateDisplay;
    }
}