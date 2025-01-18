package apps.swing;

import model.Money;
import view.AppColors;
import view.MoneyDisplay;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.DecimalFormat;

public class SwingMoneyDisplay extends JLabel implements MoneyDisplay {
    public SwingMoneyDisplay() {
        this.setText("00.00");
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setForeground(getColor(AppColors.LABEL_TEXT_COLOR));
        setBorder(createDashedBorder());
    }

    private Border createDashedBorder() {
        return BorderFactory.createDashedBorder(getColor(AppColors.LABEL_TEXT_COLOR));
    }

    private Color getColor(AppColors color) {
        return color.get();
    }

    @Override
    public void show(Money money) {
        this.setText(toString(money) + getSymbol(money));
    }

    private static String getSymbol(Money money) {
        return money.currency().symbol();
    }

    private static String toString(Money money) {
        DecimalFormat formatter = new DecimalFormat("00.000");
        return formatter.format(money.amount());
    }
}