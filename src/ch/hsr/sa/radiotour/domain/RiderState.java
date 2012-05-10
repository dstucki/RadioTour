package ch.hsr.sa.radiotour.domain;

import android.graphics.Color;

public enum RiderState {

	ACTIV(Color.TRANSPARENT, Color.WHITE), DEFECT(
			Color.parseColor("#30979914"), Color.parseColor("#ffffff")), DOCTOR(
			Color.parseColor("#30fc0000"), Color.parseColor("#ffffff")), GIVEUP(
			Color.parseColor("#30985814"), Color.parseColor("#ffffff")), FALL(
			Color.parseColor("#306e0e37"), Color.parseColor("#ffffff")), ACTIV_SELECTED(
			Color.parseColor("#80e1e1e1"), Color.parseColor("#ffffff")), NOT_STARTED(
			Color.parseColor("#30000000"), Color.parseColor("#cacaca"));

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
