package ch.hsr.sa.radiotour.activities;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.RiderPickerAdapter;
import ch.hsr.sa.radiotour.adapter.VirtualRankingAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.dialogs.EditRiderDialog;
import ch.hsr.sa.radiotour.dialogs.FileExplorerDialog;
import ch.hsr.sa.radiotour.dialogs.TimePickerDialog;
import ch.hsr.sa.radiotour.dialogs.JudgementDialog;
import ch.hsr.sa.radiotour.dialogs.KmPickerDialog;
import ch.hsr.sa.radiotour.dialogs.MaillotDialog;
import ch.hsr.sa.radiotour.dialogs.MarchTableDialog;
import ch.hsr.sa.radiotour.dialogs.SpecialRankingDialog;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.domain.MaillotStageConnection;
import ch.hsr.sa.radiotour.domain.RaceSituation;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.fragments.AdminFragment;
import ch.hsr.sa.radiotour.fragments.HeaderFragment;
import ch.hsr.sa.radiotour.fragments.RaceFragment;
import ch.hsr.sa.radiotour.fragments.SpecialRankingFragment;
import ch.hsr.sa.radiotour.fragments.VirtualRankingFragment;
import ch.hsr.sa.radiotour.fragments.interfaces.TimePickerIF;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;
import ch.hsr.sa.radiotour.technicalservices.importer.AsyncSetUpTask;
import ch.hsr.sa.radiotour.technicalservices.listener.GPSLocationListener;
import ch.hsr.sa.radiotour.technicalservices.sharedpreferences.SharedPreferencesHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * The Class RadioTourActivity. It's the only {@link Activity} in this Project
 * and acts as the starting point at the application startup
 * 
 */
public class RadioTourActivity extends Activity implements Observer,
		OnClickListener {

	protected Dialog mSplashDialog;
	private final Set<Integer> checkedIntegers = new TreeSet<Integer>();
	private final Set<TextView> checkedViews = new HashSet<TextView>();
	private RaceFragment raceFragment;
	private VirtualRankingFragment rankingFragment;
	private AdminFragment adminFragment;
	private SpecialRankingFragment specialRankingFragment;
	private DatabaseHelper databaseHelper = null;
	private RadioTour application;

	public Set<Integer> getCheckedIntegers() {
		return checkedIntegers;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showSplashScreen();
		new AsyncSetUpTask().execute(this);
	}

	/**
	 * Shows the {@link RaceFragment} after the SplashScreen disappears. This
	 * Method should only be called once
	 */
	public void showRaceFragment() {
		setContentView(R.layout.base_activity);
		raceFragment = new RaceFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.changeLayout, raceFragment);
		fragmentTransaction.commit();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		boolean data = false;
		if (mSplashDialog != null) {
			data = true;
			removeSplashScreen();
		}
		return data;
	}

	/**
	 * Removes the splash screen.
	 */
	public void removeSplashScreen() {
		if (mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}

	/**
	 * Shows splash screen.
	 */
	protected void showSplashScreen() {
		mSplashDialog = new Dialog(this, R.style.SplashScreen);
		mSplashDialog.setContentView(R.layout.splash_screen);
		mSplashDialog.setCancelable(false);
		mSplashDialog.show();

		final Handler handler = new Handler();
		handler.removeCallbacks(new Runnable() {
			@Override
			public void run() {
				removeSplashScreen();
			}
		}, 2000);
	}

	/**
	 * Sets the up domain objects. Loads the Objects from the Database into
	 * memory and keeps it at the {@link RadioTour} class
	 */
	public void setUpDomainObjects() {
		application = (RadioTour) getApplication();
		application.clearInfos();
		databaseHelper = DatabaseHelper.getHelper(this);

		for (Team team : databaseHelper.getTeamDao().queryForAll()) {
			application.add(team);
		}

		for (Rider rider : databaseHelper.getRiderDao().queryForAll()) {
			application.add(rider);
		}

		SharedPreferencesHelper.initializePreferences(getApplicationContext());

		Stage stage = databaseHelper.getStageDao().queryForId(
				SharedPreferencesHelper.preferences().getSelectedStage());

		if (stage == null) {
			stage = new Stage(getResources().getString(R.string.start),
					getResources().getString(R.string.destination));
			stage.setWholeDistance(1337D);
			databaseHelper.getStageDao().create(stage);
		}

		updateStage(stage);

		List<RiderStageConnection> conns = databaseHelper.getRiderStageDao()
				.queryForEq("etappe", stage);
		if (conns.size() == 0) {
			RiderStageConnection conn;
			for (Rider rider : application.getRiders()) {
				conn = new RiderStageConnection(stage, rider);
				databaseHelper.getRiderStageDao().create(conn);
				application.add(conn);
			}
		} else {
			for (RiderStageConnection conn : conns) {
				application.add(conn);
			}
		}
		List<MaillotStageConnection> connsMaillot = databaseHelper
				.getMaillotStageDao().queryForEq("etappe", stage);
		for (MaillotStageConnection con : connsMaillot) {
			application.addMaillotStage(con);
		}

	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			final TextView temp = (TextView) v;
			Integer checkedID;
			final String textViewString = temp.getText().toString();
			try {
				checkedID = Integer.valueOf(textViewString);
			} catch (NumberFormatException e) {
				final Integer indexSpace = textViewString.indexOf(" ");
				checkedID = Integer.valueOf(textViewString.substring(0,
						indexSpace));
			}
			final RiderStageConnection conn = application
					.getRiderStage(checkedID);
			if (conn.getRiderState() != RiderState.ACTIV) {
				conn.setRiderState(RiderState.ACTIV);
				databaseHelper.getRiderStageDao().update(conn);
			}

			if (checkedIntegers.contains(checkedID)) {
				checkedIntegers.remove(checkedID);
				checkedViews.remove(temp);
				temp.setBackgroundColor(RiderState.ACTIV.getBackgroundColor());
				temp.setTextColor(RiderState.ACTIV.getTextColor());

			} else {
				checkedIntegers.add(checkedID);
				checkedViews.add(temp);
				temp.setBackgroundColor(RiderState.ACTIV_SELECTED
						.getBackgroundColor());
				temp.setTextColor(RiderState.ACTIV_SELECTED.getTextColor());
			}
		}
	}

	/**
	 * On row layout click.
	 * 
	 * @param tableRow
	 *            the table row
	 * @param dragObject
	 *            the drag object
	 */
	public void onRowLayoutClick(View tableRow, Set<Integer> dragObject) {
		raceFragment.getGroupFragment().moveRiderNr(tableRow, dragObject);
		clearCheckedIntegers();
	}

	/**
	 * Clear checked integers onclick.
	 * 
	 * @param v
	 *            the v
	 */
	public void clearCheckedIntegersOnclick(View v) {
		clearCheckedIntegers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	/**
	 * On race button click.
	 * 
	 * @param v
	 *            the v
	 */
	public void onRaceButtonClick(View v) {
		if (!raceFragment.isAdded()) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.changeLayout, raceFragment);
			fragmentTransaction.commit();
		}
	}

	/**
	 * On ranking button click.
	 * 
	 * @param v
	 *            the v
	 */
	public void onRankingButtonClick(View v) {
		clearCheckedIntegers();
		rankingFragment = rankingFragment == null ? new VirtualRankingFragment()
				: rankingFragment;
		if (!rankingFragment.isAdded()) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.changeLayout,
					new VirtualRankingFragment());
			fragmentTransaction.commit();
		}
	}

	/**
	 * On admin button click.
	 * 
	 * @param v
	 *            the v
	 */
	public void onAdminButtonClick(View v) {
		adminFragment = new AdminFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.changeLayout, adminFragment);
		fragmentTransaction.commit();
	}

	/**
	 * On special button click.
	 * 
	 * @param v
	 *            the v
	 */
	public void onSpecialButtonClick(View v) {
		specialRankingFragment = new SpecialRankingFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.changeLayout, specialRankingFragment);
		fragmentTransaction.commit();

	}

	/**
	 * Clears the selection at the {@link RiderPickerAdapter}
	 */
	private void clearCheckedIntegers() {
		checkedIntegers.clear();
		for (TextView v : checkedViews) {
			v.setBackgroundColor(Color.TRANSPARENT);
			v.setTextColor(Color.WHITE);
		}
		checkedViews.clear();
	}

	/**
	 * Show time dialog.
	 * 
	 * @param timePickerIF
	 *            an instance of timePickerIF that the new time will be sent to
	 * @param useHour
	 *            boolean that indicates if the Dialog should only show
	 *            seconds&minutes or additionally hours
	 */
	public void showTimeDialog(TimePickerIF timePickerIF, boolean useHour) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		TimePickerDialog newFragment = new TimePickerDialog(timePickerIF, useHour);
		newFragment.show(ft, "dialog");
	}

	/**
	 * Shows km dialog that updates the {@link GPSLocationListener} distance.
	 * 
	 * @param gps
	 *            Object where you want to update the amount of km
	 */
	public void showKmDialog(GPSLocationListener gps) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		KmPickerDialog newKmPicker = new KmPickerDialog(gps);
		newKmPicker.show(ft, "dialog");

	}

	/**
	 * Show rider dialog to lookup further information to the rider or edit
	 * some. Because showing a {@link Dialog} is asynchronous, the adapter has
	 * to be given as an argument too
	 * 
	 * @param conn
	 *            {@link RiderStageConnection} Object that you want it's
	 *            assosciated {@link Rider}
	 * @param adapter
	 *            adapter to notify that the dataset has changed
	 */
	public void showRiderDialog(RiderStageConnection conn,
			VirtualRankingAdapter adapter) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("riderDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		EditRiderDialog newFragment = new EditRiderDialog(conn, adapter);
		newFragment.show(ft, "riderDialog");
	}

	/**
	 * Shows the marchTable of the actual selected {@link Stage}
	 */
	public void showMarchTableDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("marchDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		MarchTableDialog newFragment = new MarchTableDialog(
				application.getActualSelectedStage());
		newFragment.show(ft, "marchDialog");
	}

	/**
	 * Shows the dialog to create a new Maillot.Because showing a {@link Dialog}
	 * is asynchronous, the fragment has to be given as an argument too
	 * 
	 * @param fragment
	 *            the fragment object from where you are calling this dialog
	 */
	public void showMaillotDialog(AdminFragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("maillotDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		MaillotDialog newFragment = new MaillotDialog(fragment, null, null);
		newFragment.show(ft, "maillotDialog");
	}

	/**
	 * Dialog to Edit a Maillot or set the winning Driver.Because showing a
	 * {@link Dialog} is asynchronous, the fragment has to be given as an
	 * argument too
	 * 
	 * @param fragment
	 *            the fragment from where you are calling this method
	 * @param maillot
	 *            the maillot that you want to edit
	 * @param stage
	 *            the stage where you want to set the driver for
	 */
	public void editMaillotDialog(AdminFragment fragment, Maillot maillot,
			Stage stage) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("maillotDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		MaillotDialog newFragment = new MaillotDialog(fragment, maillot, stage);
		newFragment.show(ft, "maillotDialog");
	}

	@Override
	public void update(Observable observable, Object data) {
		onRowLayoutClick((View) data, checkedIntegers);
	}

	/**
	 * Because showing a {@link Dialog} is asynchronous, the fragment has to be
	 * given as an argument too
	 * 
	 * @param fragment
	 *            the fragment where this method is called from
	 * @param selectedItem
	 *            the selected item to be edited, <code>null</code> if a new
	 *            {@link SpecialRanking} should be created
	 */
	public void showSpecialRankingDialog(SpecialRankingFragment fragment,
			SpecialRanking selectedItem) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(
				"specialRankingDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		SpecialRankingDialog newFragment = new SpecialRankingDialog(fragment,
				selectedItem);
		newFragment.show(ft, "textViewDialog");
	}

	/**
	 * Show a {@link Dialog} to to edit an {@link Judgement}.Because showing a
	 * {@link Dialog} is asynchronous, the fragment has to be given as an
	 * argument too;
	 * 
	 * @param fragment
	 *            the fragment that the method is called from
	 * @param judgement
	 *            the judgement that is wanted to be edited
	 */
	public void showTextViewDialog(SpecialRankingFragment fragment,
			Judgement judgement) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(
				"specialRankingDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		JudgementDialog newFragment = new JudgementDialog(fragment, judgement);
		newFragment.show(ft, "textViewDialog");
	}

	/**
	 * Show file explorer dialog to choose an csv file to import data into the
	 * application. Because showing a {@link Dialog} is asynchronous, the
	 * fragment has to be given as an argument too
	 * 
	 * @param fragment
	 *            the fragment where this method is called from;
	 */
	public void showFileExplorerDialog(AdminFragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(
				"fileExplorerFragment");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		FileExplorerDialog newFragment = new FileExplorerDialog(fragment);
		newFragment.show(ft, "fileExplorerFragment");
	}

	@Override
	protected void onPause() {
		super.onPause();
		HeaderFragment.saveDistance();
		HeaderFragment.saveRaceTime();
	}

	/**
	 * Call this method when the actualStage changes to set the actual
	 * {@link RaceSituation} and other {@link Stage} assigned infos right in the
	 * {@link RadioTour} class
	 * 
	 * @param actualStage
	 *            the new actual stage
	 */
	public void updateStage(Stage actualStage) {
		HeaderFragment h = ((HeaderFragment) getFragmentManager()
				.findFragmentById(R.id.headerFragment));
		if (h != null) {
			h.updateStage(actualStage);
		}
		if (!newSelectionEqualsOldSelection(actualStage)) {
			try {
				application.setSituation(getNewestRaceSituation(actualStage));
			} catch (SQLException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
			}
		}
		application.setActualSelectedStage(actualStage);

	}

	/**
	 * helper method that checks whether the given stage is the same as the
	 * actual stage
	 * 
	 */
	private boolean newSelectionEqualsOldSelection(Stage actualStage) {
		return application.getActualSelectedStage() != null
				&& actualStage.getId() == application.getActualSelectedStage()
						.getId();
	}

	/**
	 * Gets the latest race situation assigned to the given Stage
	 * 
	 * @param stage
	 *            the {@link Stage} to what you want the latest
	 *            {@link RaceSituation}
	 * @return the latest {@link RaceSituation} for the actual {@link Stage}
	 * @throws SQLException
	 * 
	 */
	private RaceSituation getNewestRaceSituation(Stage stage)
			throws SQLException {
		RuntimeExceptionDao<RaceSituation, Long> raceSituationDao = databaseHelper
				.getRaceSituationDao();
		QueryBuilder<RaceSituation, Long> qb = raceSituationDao.queryBuilder();
		qb.orderBy("timestamp", false).where().eq("etappe", stage);
		qb.limit(1L);
		RaceSituation rs = raceSituationDao.queryForFirst(qb.prepare());
		if (rs == null) {
			rs = new RaceSituation(0, stage);
			databaseHelper.getRaceSituationDao().create(rs);
			return rs;
		}
		rs.addAll(databaseHelper.getGroupDao().queryForEq("racesituation", rs));
		return rs;
	}

	/**
	 * Helper Method used for Testing only
	 */
	public void dropAndCreateTables() {
		DatabaseHelper helper = new DatabaseHelper(this);
		helper.onUpgrade(null, helper.getConnectionSource(), 0, 1);
	}

}
