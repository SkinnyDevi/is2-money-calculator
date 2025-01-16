package apps.swing;

import api.CurrencyAPI;

import javax.swing.*;
import java.awt.*;

public class SwingMainFrame extends JFrame {
    private final CurrencyAPI api;

    public SwingMainFrame(CurrencyAPI api) throws HeadlessException {
        this.api = api;
        this.setTitle("Money Calculator");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
}