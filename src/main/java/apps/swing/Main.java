package apps.swing;

import api.CurrencyAPI;
import api.mock.MockAPI;

public class Main {
    public static void main(String[] args) {
        CurrencyAPI api = new MockAPI();
        SwingMainFrame mainFrame = new SwingMainFrame(api);
        mainFrame.setVisible(true);
    }
}