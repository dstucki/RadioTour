package ch.hsr.sa.radiotour.application;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.domain.BicycleRider;

import android.app.Application;

public class RadioTour extends Application {
	private ArrayList<BicycleRider> riders = new ArrayList<BicycleRider>();

	public RadioTour() {
		super();
		riders.add(new BicycleRider(1, "Elmiger Martin", "Ag2r la Mondiale", "ALM", "FRA",
				null));
		riders.add(new BicycleRider(2, "Champion Dimitri", "Ag2r la Mondiale", "ALM",
				"FRA", null));
		riders.add(new BicycleRider(3, "Goddaert Kristof", "Ag2r la Mondiale", "ALM",
				"FRA", null));
		riders.add(new BicycleRider(4, "Houanard Steve", "Ag2r la Mondiale", "ALM", "FRA",
				null));
		riders.add(new BicycleRider(5, "Lemarchand Romain", "Ag2r la Mondiale", "ALM",
				"FRA", null));
		riders.add(new BicycleRider(6, "Loubet Julien", "Ag2r la Mondiale", "ALM", "FRA",
				null));
		riders.add(new BicycleRider(7, "Mondory Lloyd", "Ag2r la Mondiale", "ALM", "FRA",
				null));
		riders.add(new BicycleRider(8, "Ravard Anthony", "Ag2r la Mondiale", "ALM", "FRA",
 null));

	}

	public ArrayList<BicycleRider> getRiders() {
		return riders;
	}

	public void setRiders(ArrayList<BicycleRider> riders) {
		this.riders = riders;
	}

}
