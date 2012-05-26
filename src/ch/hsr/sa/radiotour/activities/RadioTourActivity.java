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
import ch.hsr.sa.radiotour.adapter.VirtualRankingAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.dialogs.EditRiderDialog;
import ch.hsr.sa.radiotour.dialogs.FileExplorerDialog;
import ch.hsr.sa.radiotour.dialogs.FragmentDialog;
import ch.hsr.sa.radiotour.dialogs.JudgementDialog;
import ch.hsr.sa.radiotour.dialogs.KmPickerDialog;
import ch.hsr.sa.radiotour.dialogs.MaillotDialog;
import ch.hsr.sa.radiotour.dialogs.MarchTableDialog;
import ch.hsr.sa.radiotour.dialogs.SpecialRankingDialog;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.RaceSituation;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.fragments.AdminFragment;
import ch.hsr.sa.radiotour.fragments.HeaderFragment;
import ch.hsr.sa.radiotour.fragments.RaceFragment;
import ch.hsr.sa.radiotour.fragments.SpecialRakingFragment;
import ch.hsr.sa.radiotour.fragments.VirtualRankingFragment;
import ch.hsr.sa.radiotour.fragments.interfaces.TimePickerIF;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;
import ch.hsr.sa.radiotour.technicalservices.importer.AsyncSetUpTask;
import ch.hsr.sa.radiotour.technicalservices.listener.GPSLocationListener;
import ch.hsr.sa.radiotour.technicalservices.sharedpreferences.SharedPreferencesHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class RadioTourActivity extends Activity implements Observer,
		OnClickListener {

	protected Dialog mSplashDialog;

	private final Set<Integer> checkedIntegers = new TreeSet<Integer>();
	private final Set<TextView> checkedViews = new HashSet<TextView>();
	private RaceFragment raceFragment;
	private VirtualRankingFragment rankingFragment;
	private AdminFragment adminFragment;
	private SpecialRakingFragment specialRankingFragment;
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

	public void removeSplashScreen() {
		if (mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}

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

	public void setUpDomainObjects() {
		application = (RadioTour) getApplication();
		application.clearInfos();

		for (BicycleRider rider : getHelper().getBicycleRiderDao()
				.queryForAll()) {
			application.add(rider);
		}

		SharedPreferencesHelper.initializePreferences(getApplicationContext());

		Stage stage = getHelper().getStageDao().queryForId(
				SharedPreferencesHelper.preferences().getSelectedStage());

		if (stage == null) {
			stage = new Stage(getResources().getString(R.string.start),
					getResources().getString(R.string.destination));
			stage.setWholeDistance(1337D);
			getHelper().getStageDao().create(stage);
		}

		updateStage(stage);

		List<RiderStageConnection> conns = getHelper().getRiderStageDao()
				.queryForEq("etappe", stage);
		if (conns.size() == 0) {
			RiderStageConnection conn;
			for (BicycleRider rider : application.getRiders()) {
				conn = new RiderStageConnection(stage, rider);
				getHelper().getRiderStageDao().create(conn);
				application.add(conn);
			}
		} else {
			for (RiderStageConnection conn : conns) {
				application.add(conn);
			}
		}
		for (Team team : application.getTeams()) {
			databaseHelper.getTeamDao().create(team);
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
			conn.setRiderState(RiderState.ACTIV);
			getHelper().getRiderStageDao().update(conn);

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

	public void onRowLayoutClick(View tableRow, Set<Integer> dragObject) {
		raceFragment.getGroupFragment().moveDriverNr(tableRow, dragObject);
		clearCheckedIntegers();
	}

	public void clearCheckedIntegersOnclick(View v) {
		clearCheckedIntegers();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	public DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	public void onRaceButtonClick(View v) {
		if (!raceFragment.isAdded()) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.changeLayout, raceFragment);
			fragmentTransaction.commit();
		}
	}

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

	public void onAdminButtonClick(View v) {
		adminFragment = new AdminFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.changeLayout, adminFragment);
		fragmentTransaction.commit();
	}

	public void onSpecialButtonClick(View v) {
		specialRankingFragment = new SpecialRakingFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.changeLayout, specialRankingFragment);
		fragmentTransaction.commit();

	}

	private void clearCheckedIntegers() {
		checkedIntegers.clear();
		for (TextView v : checkedViews) {
			v.setBackgroundColor(Color.TRANSPARENT);
			v.setTextColor(Color.WHITE);
		}
		checkedViews.clear();
	}

	public void showTimeDialog(TimePickerIF timePickerIF, boolean useHour) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		FragmentDialog newFragment = new FragmentDialog(timePickerIF, useHour);
		newFragment.show(ft, "dialog");
	}

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

	public void showMarchTableDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("marchDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		MarchTableDialog newFragment = new MarchTableDialog();
		newFragment.show(ft, "marchDialog");
	}

	public void showMaillotDialog(AdminFragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("maillotDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		MaillotDialog newFragment = new MaillotDialog(fragment);
		newFragment.show(ft, "maillotDialog");
	}

	@Override
	public void update(Observable observable, Object data) {
		onRowLayoutClick((View) data, checkedIntegers);
	}

	public void showSpecialRankingDialog(SpecialRakingFragment fragment,
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

	public void showTextViewDialog(SpecialRakingFragment fragment,
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

	public void updateStage(Stage actualStage) {
		if (newSelectionEqualsOldSelection(actualStage)) {
			return;
		}
		application.setActualSelectedStage(actualStage);
		try {
			application.setSituation(getNewestRaceSituation(actualStage));
		} catch (SQLException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
		HeaderFragment h = ((HeaderFragment) getFragmentManager()
				.findFragmentById(R.id.headerFragment));
		if (h != null) {
			h.updateStage(actualStage);
		}
	}

	private boolean newSelectionEqualsOldSelection(Stage actualStage) {
		return application.getActualSelectedStage() != null
				&& actualStage.getId() == application.getActualSelectedStage()
						.getId();
	}

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
		rs.addAll(getHelper().getGroupDao().queryForEq("racesituation", rs));
		return rs;
	}

}
