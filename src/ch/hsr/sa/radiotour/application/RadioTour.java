package ch.hsr.sa.radiotour.application;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.domain.BicycleRider;

import android.app.Application;

public class RadioTour extends Application {
	private ArrayList<BicycleRider> riders = new ArrayList<BicycleRider>();

	public ArrayList<BicycleRider> getRiders() {
		return riders;
	}

	public void setRiders(ArrayList<BicycleRider> riders) {
		this.riders = riders;
	}

}
