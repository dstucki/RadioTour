package ch.hsr.sa.radiotour.domain;

import android.graphics.Color;

public enum RiderState {
	ACTIV(Color.TRANSPARENT, Color.WHITE), DEFECT(Color.TRANSPARENT, Color.GRAY), DOCTOR(
			Color.GREEN, Color.WHITE), GIVEUP(Color.RED, Color.GRAY), FALL(
			Color.BLUE, Color.BLACK);

	private final int backgroundColor, textColor;

	RiderState(int backgroundColor, int textColor) {
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public int getTextColor() {
		return textColor;
	}
}
