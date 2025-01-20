package software.ulpgc.moneycalculator.apps.swing;

import software.ulpgc.moneycalculator.api.CurrencyAPI;
import software.ulpgc.moneycalculator.api.freecurrency.FreeCurrencyAPI;
import software.ulpgc.moneycalculator.api.mock.MockAPI;
import software.ulpgc.moneycalculator.control.Command;
import software.ulpgc.moneycalculator.control.CommandIdentifiers;
import software.ulpgc.moneycalculator.control.MoneyPresenter;
import software.ulpgc.moneycalculator.control.SwapDialogContentsCommand;
import software.ulpgc.moneycalculator.model.Currency;

import java.util.List;

public class Main {
    private static final boolean USE_ONLINE_API = true;

    public static void main(String[] args) {
        CurrencyAPI api = getAppApi();
        List<Currency> currencies = api.getCurrencies();
        SwingMainFrame mainFrame = new SwingMainFrame(currencies);
        MoneyPresenter moneyPresenter = createAppPresenter(mainFrame, api);
        attachCommandsTo(mainFrame, moneyPresenter);
        mainFrame.setVisible(true);
    }

    private static CurrencyAPI getAppApi() {
        return USE_ONLINE_API ? new FreeCurrencyAPI() : new MockAPI();
    }

    private static MoneyPresenter createAppPresenter(SwingMainFrame mainFrame, CurrencyAPI api) {
        return new MoneyPresenter(
                mainFrame.getMoneyDisplay(),
                mainFrame.getMoneyDialog(),
                mainFrame.getCurrencyDialog(),
                api,
                mainFrame.getExchangeRateDisplay()
        );
    }

    private static void attachCommandsTo(SwingMainFrame mainFrame, MoneyPresenter moneyPresenter) {
        mainFrame.put(CommandIdentifiers.SWAP_DIALOG_CONTENTS, createExchangeCommand(moneyPresenter));
    }

    private static Command createExchangeCommand(MoneyPresenter moneyPresenter) {
        return new SwapDialogContentsCommand(moneyPresenter);
    }
}