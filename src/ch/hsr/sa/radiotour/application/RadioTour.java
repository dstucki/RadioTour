package ch.hsr.sa.radiotour.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Application;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.Team;

public class RadioTour extends Application {
	private final LinkedHashMap<Integer, BicycleRider> riders = new LinkedHashMap<Integer, BicycleRider>();
	private final LinkedHashMap<String, Team> teams = new LinkedHashMap<String, Team>();

	public RadioTour() {
		super();

	}

	public ArrayList<BicycleRider> getRiders() {
		return new ArrayList<BicycleRider>(riders.values());
	}

	public Map<Integer, BicycleRider> getRidersAsMap() {
		return riders;
	}

	public ArrayList<Team> getTeams() {
		return new ArrayList<Team>(teams.values());
	}

	public void add(BicycleRider rider) {
		riders.put(rider.getStartNr(), rider);
		if (!teams.containsKey(rider.getTeam())) {
			teams.put(rider.getTeam(), new Team(rider.getTeam()));
		}
		teams.get(rider.getTeam()).getDriverNumbers().add(rider.getStartNr());
	}

}
