package apps.swing;

import api.CurrencyAPI;
import api.freecurrency.FreeCurrencyAPI;
import api.mock.MockAPI;
import control.Command;
import control.CommandIdentifiers;
import control.MoneyControl;
import control.SwapDialogContentsCommand;
import model.Currency;

import java.util.List;

public class Main {
    private static final boolean USE_ONLINE_API = false;

    public static void main(String[] args) {
        CurrencyAPI api = getAppApi();
        List<Currency> currencies = api.getCurrencies();
        SwingMainFrame mainFrame = new SwingMainFrame(currencies);
        MoneyControl moneyControl = createAppController(mainFrame, api);
        attachCommandsTo(mainFrame, moneyControl);
        mainFrame.setVisible(true);
    }

    private static CurrencyAPI getAppApi() {
        return USE_ONLINE_API ? new FreeCurrencyAPI() : new MockAPI();
    }

    private static MoneyControl createAppController(SwingMainFrame mainFrame, CurrencyAPI api) {
        return new MoneyControl(
                mainFrame.getMoneyDisplay(),
                mainFrame.getMoneyDialog(),
                mainFrame.getCurrencyDialog(),
                api
        );
    }

    private static void attachCommandsTo(SwingMainFrame mainFrame, MoneyControl moneyControl) {
        mainFrame.put(CommandIdentifiers.SWAP_DIALOG_CONTENTS, createExchangeCommand(moneyControl));
    }

    private static Command createExchangeCommand(MoneyControl moneyControl) {
        return new SwapDialogContentsCommand(moneyControl);
    }
}