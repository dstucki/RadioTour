package ch.hsr.sa.radiotour.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Application;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.Group;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.technicalservices.sharedpreferences.SharedPreferencesHelper;

public class RadioTour extends Application {
	private final LinkedHashMap<Integer, BicycleRider> riders = new LinkedHashMap<Integer, BicycleRider>();
	private final LinkedHashMap<String, Team> teams = new LinkedHashMap<String, Team>();
	private Team latestTeam;
	private final LinkedList<Group> groups = new LinkedList<Group>();
	private Stage actualSelectedStage;

	public RadioTour() {
		super();
	}

	public ArrayList<BicycleRider> getRiders() {
		return new ArrayList<BicycleRider>(riders.values());
	}

	public ArrayList<Integer> getRiderNumbers() {
		return new ArrayList<Integer>(riders.keySet());
	}

	public Map<Integer, BicycleRider> getRidersAsMap() {
		return riders;
	}

	public ArrayList<Team> getTeams() {
		return new ArrayList<Team>(teams.values());
	}

	public void add(BicycleRider rider) {
		riders.put(rider.getStartNr(), rider);
		if (latestTeam == null || latestTeam.getDriverNumbers().size() >= 10) {
			latestTeam = new Team(rider.getTeam() + teams.size());
			teams.put(latestTeam.getName(), latestTeam);
		}
		latestTeam.getDriverNumbers().add(rider.getStartNr());

		// if (!teams.(rider.getTeam())) {
		// teams.put(rider.getTeam(), new Team(rider.getTeam()));
		// }
		// teams.get(rider.getTeam()).getDriverNumbers().add(rider.getStartNr());
	}

	public void add(int index, Group group) {
		groups.add(index, group);
	}

	public List<Group> getGroups() {
		Collections.sort(groups);
		return groups;
	}

	public Stage getActualSelectedStage() {
		return actualSelectedStage;
	}

	public void setActualSelectedStage(Stage actualSelectedStage) {
		this.actualSelectedStage = actualSelectedStage;
		SharedPreferencesHelper.preferences().setSelectedStage(
				actualSelectedStage);
	}

}
