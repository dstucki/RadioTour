package ch.hsr.sa.radiotour.fragments.controller;

import java.util.Collection;
import java.util.List;

import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Group;
import ch.hsr.sa.radiotour.domain.RaceSituation;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.fragments.HeaderFragment;
import ch.hsr.sa.radiotour.fragments.RiderGroupFragment;
import ch.hsr.sa.radiotour.technicalservices.connection.JsonSendingQueue;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * Class that acts as a Controller for {@link RiderGroupFragment}
 */
public class RiderGroupController {
	private final RadioTour app;
	private RuntimeExceptionDao<Group, Integer> groupDao;
	private RuntimeExceptionDao<RaceSituation, Long> situationDao;
	private RuntimeExceptionDao<RiderStageConnection, Integer> riderStageDao;

	/**
	 * Public Constructor
	 * 
	 * @param fragment
	 *            to which this controller is assigned
	 */
	public RiderGroupController(RiderGroupFragment fragment) {
		this.app = (RadioTour) fragment.getActivity().getApplication();
		assignDaos(DatabaseHelper.getHelper(fragment.getActivity()));
	}

	/**
	 * assigns the dao's fields
	 * 
	 * @param helper
	 *            {@link DatabaseHelper} instance
	 */
	private void assignDaos(DatabaseHelper helper) {
		groupDao = helper.getGroupDao();
		situationDao = helper.getRaceSituationDao();
		riderStageDao = helper.getRiderStageDao();
	}

	/**
	 * Persists a {@link List} of {@link Group} objects to the Database
	 * 
	 * @param gr
	 *            {@link Group} objects to be persisted
	 */
	private void create(List<Group> gr) {
		for (Group g : gr) {
			groupDao.create(g);
		}
	}

	/**
	 * Persists given {@link RaceSituation} object and the assigned
	 * {@link Group} objects to the Database. Adds the given
	 * {@link RaceSituation} object to the {@link JsonSendingQueue}
	 * 
	 * @param sit
	 *            {@link RaceSituation} that will be persisted and sent to the
	 *            server
	 */
	private void create(RaceSituation sit) {
		situationDao.create(sit);
		create(sit.getGroups());
		JsonSendingQueue.getInstance().addToQueue(sit);
	}

	/**
	 * Persist the given Object to Database
	 * 
	 * @param conn
	 *            {@link RiderStageConnection} to be persisted
	 */
	public void update(RiderStageConnection conn) {
		riderStageDao.update(conn);
	}

	/**
	 * Get all Starting Numbers that are stored in {@link RadioTour}
	 * 
	 * @return a {@link Collection} of {@link Integer}
	 */
	public Collection<Integer> getRiderNumbers() {
		return app.getRiderNumbers();
	}

	/**
	 * Get all Groups that are stored in {@link RadioTour}
	 * 
	 * @return a {@link Collection} of {@link Group}
	 */
	public Collection<Group> getGroups() {
		return app.getGroups();
	}

	/**
	 * gets the {@link RiderStageConnection} and changes the {@link RiderState}
	 * and persists it to the Database
	 * 
	 * @param riderNr
	 *            number of the rider
	 * @param state
	 *            new state that the {@link RiderStageConnection} will have
	 */
	public void setRiderState(int riderNr, RiderState state) {
		RiderStageConnection riderStage = app.getRiderStage(riderNr);
		riderStage.setRiderState(state);
		JsonSendingQueue.getInstance().addToQueue(riderStage);
		update(riderStage);
	}

	/**
	 * Creates and returns the Field {@link Group} containing all of the
	 * RiderNummbers
	 * 
	 * @return
	 */
	public Group createFieldGroup() {
		Group gr = new Group();
		gr.setSituation(app.getSituation());
		gr.setField(true);
		gr.getRiderNumbers().addAll(getRiderNumbers());
		gr.setOrderNumber(0);
		return gr;
	}

	/**
	 * Persists all the {@link RiderStageConnection} that are assigned to the
	 * actual {@link Stage} to Database
	 */
	private void updateConns() {
		Collection<Integer> riderNrs = getRiderNumbers();
		for (int i : riderNrs) {
			update(app.getRiderStage(i));
		}

	}

	/**
	 * Creates and return a new RaceSituation with the appropiate value for
	 * {@link RaceSituation} distance and Stage
	 * 
	 * @return the new createt {@link RaceSituation} object
	 */
	private RaceSituation getNewSituation() {
		return new RaceSituation(HeaderFragment.mGPS.getDistanceInKm(),
				app.getActualSelectedStage());
	}

	/**
	 * Creates a {@link RaceSituation} object, adds the groups and persists it.
	 * Additionally it adds the Situation to the {@link JsonSendingQueue}
	 * 
	 * @param groups
	 *            all the groups that should be in the current
	 *            {@link RaceSituation}
	 */
	public void saveGroups(List<Group> groups) {
		runAsThread(new SaveGroups(groups), "saveGroups");
	}

	/**
	 * Helper Class to wrap a {@link Runnable} and run it as a {@link Thread}
	 * 
	 * @param runnable
	 *            That will be ran from the {@link Thread}
	 * @param name
	 *            Name that will be assigned to the {@link Thread}
	 */
	private void runAsThread(Runnable runnable, String name) {
		new Thread(runnable, name).start();
	}

	/**
	 * Class implementing {@link Runnable} that makes all the saving of a
	 * {@link List} of Groups inclusive creating {@link RaceSituation} and
	 * persist the {@link RiderStageConnection}
	 * 
	 */
	private class SaveGroups implements Runnable {
		private final List<Group> groups;

		/**
		 * public constructor
		 * 
		 * @param groups
		 *            {@link List} of {@link Group} that will be packed into a
		 *            {@link RaceSituation} and be sent to the Server
		 */
		public SaveGroups(List<Group> groups) {
			this.groups = groups;
		}

		/**
		 * Does all the synchronization work for a given {@link List} of
		 * {@link Group} and initiates the sending to the server using the
		 * {@link JsonSendingQueue}
		 */

		@Override
		public void run() {
			RaceSituation situation = getNewSituation();

			for (Group gr : groups) {
				gr.setSituation(situation);
				gr.setOrderNumber(groups.indexOf(gr));
				situation.add(gr);
			}
			create(situation);
			app.setSituation(situation);
			updateConns();

		}

	}

}
