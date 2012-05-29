package ch.hsr.sa.radiotour.fragments.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.util.Log;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.SpecialRankingListAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.SpecialPointHolder;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.sorting.ArrayListSpecialHolderComparator;
import ch.hsr.sa.radiotour.fragments.SpecialRankingFragment;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * Class that acts as a Controller for a {@link SpecialRankingFragment} View.
 * 
 * @author std
 * 
 */
public class SpecialRankingController {
	private final SpecialRankingFragment fragment;
	private final RadioTour app;
	private RuntimeExceptionDao<SpecialRanking, Integer> specialRankingDao;
	private RuntimeExceptionDao<Judgement, Integer> judgementDao;
	private RuntimeExceptionDao<SpecialPointHolder, Integer> pointHolderDao;
	private RuntimeExceptionDao<RiderStageConnection, Integer> riderStageDao;

	/**
	 * Constructor
	 * 
	 * @param fragment
	 *            The Fragment which this object will be associated
	 */
	public SpecialRankingController(SpecialRankingFragment fragment) {
		this.fragment = fragment;
		this.app = (RadioTour) fragment.getActivity().getApplication();
		assignDaos(DatabaseHelper.getHelper(fragment.getActivity()));
	}

	/**
	 * assign the DatabaseDAO's
	 */
	private void assignDaos(DatabaseHelper helper) {
		specialRankingDao = helper.getSpecialRankingDao();
		judgementDao = helper.getJudgementDao();
		pointHolderDao = helper.getSpecialPointDao();
		riderStageDao = helper.getRiderStageDao();
	}

	/**
	 * Get all {@link SpecialPointHolder} that are associated to the given
	 * {@link Judgement}
	 * 
	 * @param judgement
	 *            the {@link Judgement} to which you want to all the
	 *            {@link SpecialPointHolder}
	 * @return a {@link List} of {@link SpecialPointHolder}
	 */
	public List<SpecialPointHolder> getPointHolder(Judgement judgement) {
		List<SpecialPointHolder> temp = pointHolderDao.queryForEq("judgement",
				judgement);
		Collections.sort(temp);
		return temp;
	}

	/**
	 * update an already persisted {@link SpecialPointHolder} object to database
	 * and recalculate the perstage bonustime for the old and new rider that are
	 * assigned to the given {@link SpecialPointHolder}
	 * 
	 * @param oldRider
	 *            the oldRider that was holding this {@link SpecialPointHolder}
	 * @param holder
	 *            Object to be updated to database
	 */
	public void update(SpecialPointHolder holder) {
		pointHolderDao.update(holder);
	}

	/**
	 * Get all {@link Judgement} that are associated to the given
	 * {@link SpecialRanking}
	 * 
	 * @param ranking
	 *            the {@link SpecialRanking} to which you want all the
	 *            {@link Judgement}
	 * @return a {@link List} of {@link Judgement}
	 */
	public List<Judgement> getJudgements(SpecialRanking ranking) {
		return judgementDao.queryForEq("specialranking", ranking);
	}

	/**
	 * Get all {@link Judgement} that are associated to {@link SpecialRanking}
	 * and the {@link Stage} given
	 * 
	 * @param ranking
	 *            the {@link SpecialRanking} to which you want all the
	 *            {@link Judgement}
	 * @param stage
	 *            the {@link Stage} to which you want all the {@link Judgement}
	 * @return a {@link List} of {@link Judgement}
	 */
	public List<Judgement> getJudgements(SpecialRanking ranking, Stage stage) {
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("specialranking", ranking);
		map.put("etappe", stage);
		return judgementDao.queryForFieldValues(map);
	}

	/**
	 * Deletes the given {@link Judgement} and all associated
	 * {@link SpecialPointHolder} in the Database
	 * 
	 * @param judgement
	 *            that's wanted to be deleted in the Database
	 */
	public void delete(Judgement judgement) {
		List<SpecialPointHolder> pointHolder = getPointHolder(judgement);
		Set<Integer> tempSet = new TreeSet<Integer>();
		for (SpecialPointHolder holder : pointHolder) {
			tempSet.add(holder.getRider().getStartNr());
			pointHolderDao.delete(holder);
		}
		judgementDao.delete(judgement);
		calculateBonis(tempSet);
	}

	/**
	 * Creates the given {@link Judgement} in the database and calls the Method
	 * {@link SpecialRankingController#createHolderObjects(Judgement)}
	 * 
	 * @param judgement
	 *            that will be created to the database
	 */
	public void createJudgement(Judgement judgement) {
		judgementDao.create(judgement);
		createHolderObjects(judgement);
	}

	/**
	 * initially creates and persist the {@link SpecialPointHolder} Objects to
	 * the database. This Method will only be called from
	 * {@link SpecialRankingController#createJudgement(Judgement)}
	 * 
	 * @param judgement
	 *            the judgement that doesn't have {@link SpecialPointHolder}
	 */
	private void createHolderObjects(Judgement judgement) {
		for (int i = 0; i < judgement.getNrOfWinningRiders(); i++) {
			SpecialPointHolder temp = new SpecialPointHolder();
			temp.setRank(i);
			temp.setJudgement(judgement);
			temp.setPointBoni(judgement.getPointBonis().get(i));
			temp.setTimeBoni(judgement.getTimeBonis().get(i));
			pointHolderDao.create(temp);
		}
	}

	/**
	 * create or update {@link SpecialRanking} object to database
	 * 
	 * @param ranking
	 *            Object to be created or updated to database
	 */
	public void createUpdateRanking(SpecialRanking ranking) {
		specialRankingDao.createOrUpdate(ranking);
	}

	/**
	 * get all Rankings from database
	 * 
	 * @return {@link List} of {@link SpecialRanking}
	 */
	public List<SpecialRanking> getAllRankings() {
		return specialRankingDao.queryForAll();
	}

	/**
	 * Delete the given {@link SpecialRanking} from the Database inclusive all
	 * associated {@link Judgement} and {@link SpecialPointHolder} . For this
	 * reason, {@link SpecialRankingController#delete(Judgement)} is been called
	 * for every {@link Judgement} that is associated with the given
	 * {@link SpecialRanking}
	 * 
	 * @param temp
	 */
	public void delete(SpecialRanking temp) {
		for (Judgement judgement : getJudgements(temp)) {
			delete(judgement);
		}
		specialRankingDao.delete(temp);

	}

	/**
	 * Create and calculate a Ranking (Rangliste) for the given
	 * {@link SpecialRanking}
	 * 
	 * @param ranking
	 * @return {@link ArrayList} of {@link SpecialPointHolder} that can passed
	 *         as an Argument to {@link SpecialRankingListAdapter}
	 */
	public ArrayList<ArrayList<SpecialPointHolder>> getVirtualMap(
			SpecialRanking ranking) {

		HashMap<Integer, ArrayList<SpecialPointHolder>> map = new HashMap<Integer, ArrayList<SpecialPointHolder>>();
		if (ranking == null) {
			return new ArrayList<ArrayList<SpecialPointHolder>>(map.values());
		}
		for (Judgement temp : getJudgements(ranking)) {
			for (SpecialPointHolder holder : getPointHolder(temp)) {
				Rider rider = holder.getRider();
				if (rider == null) {
					continue;
				}
				if (!map.containsKey(rider.getStartNr())) {
					map.put(rider.getStartNr(),
							new ArrayList<SpecialPointHolder>());
				}
				map.get(rider.getStartNr()).add(holder);
			}
		}
		ArrayList<ArrayList<SpecialPointHolder>> arrayList = new ArrayList<ArrayList<SpecialPointHolder>>(
				map.values());
		Collections.sort(arrayList, new ArrayListSpecialHolderComparator());
		return arrayList;
	}

	/**
	 * Calculate the Boniseconds and Bonitpoints for the ActualStage and the
	 * drivers provided in the ridernrs Collection
	 * 
	 * @param ridernrs
	 *            collection of the ridernrs that have changed
	 */
	public void calculateBonis(Collection<Integer> ridernrs) {
		new Thread(new UpdateBoniRunnable(ridernrs), "UpdateBoni").start();

	}

	/**
	 * Generates an Template {@link Judgement} that is associated to a
	 * {@link Stage} and a {@link SpecialRanking}
	 * 
	 * @param actualSelectedStage
	 * @param specialRanking
	 * @return the generated {@link Judgement}
	 */
	public Judgement generateJudgement(Stage actualSelectedStage,
			SpecialRanking specialRanking) {
		Judgement temp = new Judgement(
				fragment.getString(R.string.lb_newjudgement), 12.5,
				actualSelectedStage);
		temp.setRanking(specialRanking);
		return temp;
	}

	/**
	 * Runnable Class that calculates the bonus things
	 * 
	 * 
	 */
	private class UpdateBoniRunnable implements Runnable {
		private final Collection<Integer> ridernrs;

		public UpdateBoniRunnable(Collection<Integer> ridernrs) {
			this.ridernrs = ridernrs;
		}

		@Override
		public void run() {
			for (int i : ridernrs) {
				Log.i(getClass().getSimpleName(), i + "");

				if (i == 0) {
					continue;
				}
				RiderStageConnection conn = app.getRiderStage(i);
				conn.setBonusPoints(0);
				conn.setBonusTime(0);
				int bonuspoints = 0, timeboni = 0;

				for (Judgement jud : judgementDao.queryForEq("etappe",
						app.getActualSelectedStage())) {
					Map<String, Object> constraints = new HashMap<String, Object>();
					constraints.put("judgement", jud);
					constraints.put("rider", app.getRider(i));
					for (SpecialPointHolder holder : pointHolderDao
							.queryForFieldValues(constraints)) {
						bonuspoints += holder.getPointBoni();
						timeboni += holder.getTimeBoni();
					}

				}
				conn.setBonusPoints(bonuspoints);
				conn.setBonusTime(timeboni);
				riderStageDao.update(conn);
			}

		}

	}

}
