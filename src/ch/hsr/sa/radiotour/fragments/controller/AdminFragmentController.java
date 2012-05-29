package ch.hsr.sa.radiotour.fragments.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.fragments.AdminFragment;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;
import ch.hsr.sa.radiotour.technicalservices.importer.CSVReader;
import ch.hsr.sa.radiotour.technicalservices.importer.MarchTableImport;
import ch.hsr.sa.radiotour.technicalservices.importer.RiderImport;
import ch.hsr.sa.radiotour.technicalservices.importer.RiderStageImport;
import ch.hsr.sa.radiotour.technicalservices.importer.StageImport;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Class that acts as Controller to {@link AdminFragment}
 * 
 */
public class AdminFragmentController {
	private final DatabaseHelper helper;
	private RuntimeExceptionDao<Stage, Integer> stageDbDao;
	private RuntimeExceptionDao<Rider, Integer> riderDbDao;
	private RuntimeExceptionDao<RiderStageConnection, Integer> riderStageDao;
	private RuntimeExceptionDao<Maillot, Integer> maillotDao;
	private RuntimeExceptionDao<Team, String> teamDao;
	private RuntimeExceptionDao<PointOfRace, Integer> pointOfRaceDao;
	private final RadioTour app;

	public AdminFragmentController(AdminFragment fragment) {
		this.app = (RadioTour) fragment.getActivity().getApplication();
		this.helper = DatabaseHelper.getHelper(fragment.getActivity());
		assignDaos(helper);
	}

	private void assignDaos(DatabaseHelper helper) {
		stageDbDao = helper.getStageDao();
		riderDbDao = helper.getRiderDao();
		riderStageDao = helper.getRiderStageDao();
		maillotDao = helper.getMaillotRuntimeDao();
		pointOfRaceDao = helper.getPointOfRaceDao();
		teamDao = helper.getTeamDao();
	}

	public void update(Stage stage) {
		stageDbDao.update(stage);
	}

	public void update(RiderStageConnection riderStage) {
		riderStageDao.update(riderStage);
	}

	public void create(final Stage stage) {
		stageDbDao.create(stage);
		new Thread(new Runnable() {

			@Override
			public void run() {
				createRiderStageConnection(stage);
			}
		}).start();
	}

	public List<Stage> getStages() {
		return stageDbDao.queryForAll();
	}

	public void create(RiderStageConnection riderStage) {
		riderStageDao.create(riderStage);
	}

	public void delete(Stage stage) {
		riderStageDao.delete(getRiderStages(stage));
		stageDbDao.delete(stage);
	}

	public void createUpdate(Team team) {
		teamDao.createOrUpdate(team);
	}

	public List<RiderStageConnection> getRiderStages(Stage stage) {
		return riderStageDao.queryForEq("etappe", stage);
	}

	public void create(Rider rider) {
		riderDbDao.create(rider);
		createUpdate(rider.getTeam());
	}

	public List<Maillot> getMaillots() {
		return maillotDao.queryForAll();
	}

	public void importMarchTable(File file, Stage stage)
			throws FileNotFoundException {
		deletePointOfRaces(stage);
		PointOfRace point;
		MarchTableImport importutil = new MarchTableImport();
		for (String[] line : readCSV(file)) {
			point = importutil.convertTo(line);
			point.setStage(stage);
			pointOfRaceDao.create(point);
		}
	}

	public void importDriver(File file, Stage stage)
			throws FileNotFoundException {
		dropAndCreateTable(Rider.class, RiderStageConnection.class);
		Rider bicycleRider;
		RiderImport importUtil = new RiderImport(app);
		for (String[] riderAsString : readCSV(file)) {
			bicycleRider = importUtil.convertTo(riderAsString);
			create(bicycleRider);
			app.add(bicycleRider);
			final RiderStageConnection conn = new RiderStageConnection(stage,
					bicycleRider);
			app.add(conn);
			create(conn);
		}
	}

	public void importRiderStageConnections(File file)
			throws FileNotFoundException {
		List<String[]> list = readCSV(file);
		RiderStageImport importUtil = new RiderStageImport(app);
		String[] stringArrayFirst = list.get(0);
		update(importUtil.convertFirstTo(stringArrayFirst));
		for (String[] stringArray : list.subList(1, list.size())) {
			update(importUtil.convertTo(stringArray));
		}
	}

	public void importStages(File file) throws FileNotFoundException {
		dropAndCreateTable(Stage.class, RiderStageConnection.class,
				PointOfRace.class, Judgement.class, SpecialRanking.class);
		StageImport importUtil = new StageImport();
		for (String[] array : readCSV(file)) {
			create(importUtil.convertTo(array));
		}
	}

	private void deletePointOfRaces(Stage stage) {
		pointOfRaceDao.delete(pointOfRaceDao.queryForEq("etappe", stage));
	}

	private ArrayList<String[]> readCSV(File f) throws FileNotFoundException {
		CSVReader reader = new CSVReader(new FileInputStream(f));
		return reader.readFile();
	}

	private void dropAndCreateTable(Class<?>... classes) {
		ConnectionSource src = helper.getConnectionSource();
		for (int i = 0; i < classes.length; i++) {
			try {
				TableUtils.dropTable(src, classes[i], true);
				TableUtils.createTable(src, classes[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void createRiderStageConnection(final Stage stage) {
		for (Rider rider : app.getRiders()) {
			create(new RiderStageConnection(stage, rider));
		}
	}

	public void stageChanged(Stage actualStage) {
		app.getRiderPerStage().clear();
		List<RiderStageConnection> list = getRiderStages(actualStage);
		if (list.size() == 0) {
			createRiderStageConnection(actualStage);
		}
		for (RiderStageConnection conn : getRiderStages(actualStage)) {
			app.add(conn);
		}
	}

	public Stage createStage() {
		Stage temp = new Stage("Start", "Ende", 150d);
		create(temp);
		return temp;
	}
}
