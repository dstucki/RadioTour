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
import ch.hsr.sa.radiotour.domain.RaceSituation;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.technicalservices.connection.JSONConnectionQueue;
import ch.hsr.sa.radiotour.technicalservices.sharedpreferences.SharedPreferencesHelper;

public class RadioTour extends Application {
	private final LinkedHashMap<Integer, BicycleRider> riders = new LinkedHashMap<Integer, BicycleRider>();
	private final Map<Integer, RiderStageConnection> riderPerStage = new LinkedHashMap<Integer, RiderStageConnection>();
	private final LinkedHashMap<String, Team> teams = new LinkedHashMap<String, Team>();
	private final LinkedList<Group> groups = new LinkedList<Group>();
	private final static String THREAD_NAME = "serverthread";
	private Stage actualSelectedStage;
	private RaceSituation situation;

	public RadioTour() {
		super();
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				JSONConnectionQueue.getInstance().start();
			}
		}, THREAD_NAME);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	public List<BicycleRider> getRiders() {
		return new ArrayList<BicycleRider>(riders.values());
	}

	public List<Integer> getRiderNumbers() {
		return new ArrayList<Integer>(riders.keySet());
	}

	public BicycleRider getRider(int startNr) {
		return riders.get(startNr);
	}

	public List<Team> getTeams() {
		return new ArrayList<Team>(teams.values());
	}

	public void add(BicycleRider rider) {
		riders.put(rider.getStartNr(), rider);
		if (!teams.containsKey(rider.getTeam())) {
			teams.put(rider.getTeam(), new Team(rider.getTeam()));
		}
		teams.get(rider.getTeam()).getDriverNumbers().add(rider.getStartNr());
	}

	public List<Group> getGroups() {
		groups.clear();
		if (situation != null) {
			groups.addAll(situation.getGroups());
		}
		Collections.sort(groups);
		return groups;
	}

	public Stage getActualSelectedStage() {
		return actualSelectedStage;
	}

	public void setActualSelectedStage(Stage actualSelectedStage) {
		this.actualSelectedStage = actualSelectedStage;
		if (actualSelectedStage != null) {
			SharedPreferencesHelper.preferences().setSelectedStage(
					actualSelectedStage);
		}
	}

	public Map<Integer, RiderStageConnection> getRiderPerStage() {
		return riderPerStage;
	}

	public RiderStageConnection getRiderStage(int startNr) {
		return riderPerStage.get(startNr);
	}

	public void add(RiderStageConnection conn) {
		riderPerStage.put(conn.getRider().getStartNr(), conn);
	}

	public RaceSituation getSituation() {
		return situation;
	}

	public void setSituation(RaceSituation situation) {
		this.situation = situation;
	}

	public void clearInfos() {
		riders.clear();
		teams.clear();
		riderPerStage.clear();
		groups.clear();
	}
}
