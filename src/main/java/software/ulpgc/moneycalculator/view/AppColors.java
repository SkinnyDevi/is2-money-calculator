package software.ulpgc.moneycalculator.view;

import java.awt.*;

public enum AppColors {
	PANEL_BACKGROUND_COLOR(new Color(1382685)),
	BUTTON_BG_COLOR(new Color(2172201)),
	BACKGROUND_BUTTON_HOVER_COLOR(new Color(3226443)),
	LABEL_TEXT_COLOR(Color.white),
	FAINT_RATE_BORDER_COLOR(Color.DARK_GRAY);

	private final Color color;

	AppColors(Color color) {
		this.color = color;
	}

	public Color get() {
		return color;
	}
}