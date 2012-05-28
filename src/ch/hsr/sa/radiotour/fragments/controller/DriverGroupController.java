package ch.hsr.sa.radiotour.fragments.controller;

import java.util.Collection;
import java.util.List;

import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Group;
import ch.hsr.sa.radiotour.domain.RaceSituation;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.fragments.DriverGroupFragment;
import ch.hsr.sa.radiotour.fragments.HeaderFragment;
import ch.hsr.sa.radiotour.technicalservices.connection.JsonSendingQueue;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

import com.j256.ormlite.dao.RuntimeExceptionDao;

public class DriverGroupController {
	private final RadioTour app;
	private RuntimeExceptionDao<Group, Integer> groupDao;
	private RuntimeExceptionDao<RaceSituation, Long> situationDao;
	private RuntimeExceptionDao<RiderStageConnection, Integer> riderStageDao;

	public DriverGroupController(DriverGroupFragment fragment) {
		this.app = (RadioTour) fragment.getActivity().getApplication();
		assignDaos(DatabaseHelper.getHelper(fragment.getActivity()));
	}

	private void assignDaos(DatabaseHelper helper) {
		groupDao = helper.getGroupDao();
		situationDao = helper.getRaceSituationDao();
		riderStageDao = helper.getRiderStageDao();

	}

	private void create(List<Group> gr) {
		for (Group g : gr) {
			groupDao.create(g);
		}
	}

	private void create(RaceSituation sit) {
		situationDao.create(sit);
		create(sit.getGroups());
		JsonSendingQueue.getInstance().addToQueue(sit);
	}

	public void update(RiderStageConnection conn) {
		riderStageDao.update(conn);
	}

	public List<Integer> getRiderNumbers() {
		return app.getRiderNumbers();
	}

	public List<Group> getGroups() {
		return app.getGroups();
	}

	private void add(Group g) {
		app.getSituation().add(g);
	}

	public Group createFieldGroup() {
		Group gr = new Group();
		gr.setSituation(app.getSituation());
		gr.setField(true);
		gr.getDriverNumbers().addAll(app.getRiderNumbers());
		gr.setOrderNumber(0);
		add(gr);
		return gr;
	}

	public void updateConns(final Collection<Integer> ridernrs) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Collection<Integer> riderNrs = ridernrs == null ? app.getRiderNumbers()
						: ridernrs;
				for (int i : riderNrs) {
					update(app.getRiderStage(i));
				}

			}
		}, "DriverGroupController#updateConns").start();
	}

	private RaceSituation getNewSituation() {
		return new RaceSituation(HeaderFragment.mGPS.getDistanceInKm(),
				app.getActualSelectedStage());
	}

	public void saveGroups(List<Group> groups) {
		RaceSituation situation = getNewSituation();

		for (Group gr : groups) {
			gr.setSituation(situation);
			gr.setOrderNumber(groups.indexOf(gr));
			situation.add(gr);
		}
		create(situation);
	}

}
