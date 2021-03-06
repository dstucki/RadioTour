package ch.hsr.sa.radiotour.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.util.SparseArray;
import ch.hsr.sa.radiotour.domain.Group;
import ch.hsr.sa.radiotour.domain.MaillotStageConnection;
import ch.hsr.sa.radiotour.domain.RaceSituation;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.technicalservices.connection.JsonSendingQueue;
import ch.hsr.sa.radiotour.technicalservices.sharedpreferences.SharedPreferencesHelper;

/**
 * Class extending {@link Application} where the domain-objects are stored for
 * better performance then always looking them up in the database
 */
public class RadioTour extends Application {
	private final LinkedHashMap<Integer, Rider> riders = new LinkedHashMap<Integer, Rider>();
	private final Map<Integer, RiderStageConnection> riderPerStage = new LinkedHashMap<Integer, RiderStageConnection>();
	private final LinkedHashMap<String, Team> teams = new LinkedHashMap<String, Team>();
	private final LinkedList<Group> groups = new LinkedList<Group>();
	private final SparseArray<MaillotStageConnection> maillots = new SparseArray<MaillotStageConnection>();
	private final static String THREAD_NAME = "serverthread";
	private Stage actualSelectedStage;
	private RaceSituation situation;

	/**
	 * Non-Args Constructor that starts the {@link JsonSendingQueue} in a thread
	 */
	public RadioTour() {
		super();
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				JsonSendingQueue.getInstance().start();
			}
		}, THREAD_NAME);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	public List<Rider> getRiders() {
		return new ArrayList<Rider>(riders.values());
	}

	public void addMaillotStage(MaillotStageConnection conn) {
		if (conn == null || conn.getRider() == null) {
			return;
		}
		maillots.put(conn.getRider().getStartNr(), conn);
	}

	public MaillotStageConnection getMaillotStage(int riderNr) {
		return maillots.get(riderNr);
	}

	public SparseArray<MaillotStageConnection> getMaillotStages() {
		return maillots;
	}

	public List<Integer> getRiderNumbers() {
		return new ArrayList<Integer>(riders.keySet());
	}

	public Rider getRider(int startNr) {
		return riders.get(startNr);
	}

	public List<Team> getTeams() {
		return new ArrayList<Team>(teams.values());
	}

	public void add(Rider rider) {
		riders.put(rider.getStartNr(), rider);
		if (!teams.containsKey(rider.getTeam().getName())) {
			teams.put(rider.getTeam().getName(), new Team(rider.getTeam()
					.getName()));
		}
		teams.get(rider.getTeam().getName()).addRider(rider);
	}

	public Team getTeam(String teamName) {
		return teams.get(teamName);
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

	/**
	 * clears all the Information holded here in memory
	 */
	public void clearInfos() {
		riders.clear();
		teams.clear();
		riderPerStage.clear();
		groups.clear();
		maillots.clear();
	}

	public void add(Team team) {
		teams.put(team.getName(), team);
	}
}
