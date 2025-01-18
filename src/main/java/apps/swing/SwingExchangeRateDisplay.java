package apps.swing;

import model.ExchangeRate;
import view.AppColors;
import view.ExchangeRateDisplay;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.DecimalFormat;

public class SwingExchangeRateDisplay extends JLabel implements ExchangeRateDisplay {
    public SwingExchangeRateDisplay() {
        this.setText("Rate = 1.0000");
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setForeground(getColor(AppColors.LABEL_TEXT_COLOR));
        setBorder(createLineBorder());
    }

    private Border createLineBorder() {
        return BorderFactory.createLineBorder(getColor(AppColors.FAINT_RATE_BORDER_COLOR));
    }

    private Color getColor(AppColors color) {
        return color.get();
    }

    @Override
    public void show(ExchangeRate rate) {
        this.setText("Rate = " + toString(rate) + getSymbol(rate));
    }

    private static String getSymbol(ExchangeRate rate) {
        return rate.to().symbol();
    }

    private static String toString(ExchangeRate rate) {
        DecimalFormat formatter = new DecimalFormat("0.0000");
        return formatter.format(rate.rate());
    }
}