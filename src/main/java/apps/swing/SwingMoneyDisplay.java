package apps.swing;

import model.Money;
import view.MoneyDisplay;

import javax.swing.*;
import java.text.DecimalFormat;

public class SwingMoneyDisplay extends JLabel implements MoneyDisplay {
    public SwingMoneyDisplay() {
        this.setText("00.00");
        this.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public void show(Money money) {
        this.setText(toString(money) + getSymbol(money));
    }

    private static String getSymbol(Money money) {
        return money.currency().symbol();
    }

    private static String toString(Money money) {
        DecimalFormat formatter = new DecimalFormat("00.00");
        return formatter.format(money.amount());
    }
}