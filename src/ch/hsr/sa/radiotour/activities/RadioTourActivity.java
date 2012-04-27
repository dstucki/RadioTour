package ch.hsr.sa.radiotour.activities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.fragments.RaceFragment;
import ch.hsr.sa.radiotour.fragments.VirtualRankingFragment;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;
import ch.hsr.sa.radiotour.technicalservices.importer.CSVReader;
import ch.hsr.sa.radiotour.views.FragmentDialog;
import ch.hsr.sa.radiotour.views.GroupTableRow;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class RadioTourActivity extends Activity implements Observer {

	private final TreeSet<Integer> checkedIntegers = new TreeSet<Integer>();
	private final HashSet<TextView> checkedViews = new HashSet<TextView>();
	private RaceFragment raceFragment;
	private VirtualRankingFragment rankingFragment;

	public TreeSet<Integer> getCheckedIntegers() {
		return checkedIntegers;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		importDriverandTeams();
		setContentView(R.layout.base_activity);
		raceFragment = new RaceFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.changeLayout, raceFragment);
		fragmentTransaction.commit();

	}

	public void importDriverandTeams() {
		for (BicycleRider rider : getHelper().getBicycleRiderDao()
				.queryForAll()) {
			((RadioTour) getApplication()).add(rider);
		}
		((RadioTour) getApplication()).getGroups().addAll(
				databaseHelper.getGroupDao().queryForAll());
		Collections.sort(((RadioTour) getApplication()).getGroups());
		Log.e(getClass().getSimpleName(), ((RadioTour) getApplication())
				.getGroups().size() + "");
		if (((RadioTour) getApplication()).getRiders().size() <= 0) {
			CSVReader reader = new CSVReader(getResources().openRawResource(
					R.raw.startliste));
			for (String[] riderAsString : reader.readFile()) {
				((RadioTour) getApplication())
						.add(convertStringArrayToRider(riderAsString));
			}

		}
		for (Team team : ((RadioTour) getApplication()).getTeams()) {
			databaseHelper.getTeamDao().create(team);
		}
	}

	private BicycleRider convertStringArrayToRider(String[] riderAsString) {
		try {
			BicycleRider bicycleRider = new BicycleRider(
					Integer.valueOf(riderAsString[0]), riderAsString[1],
					riderAsString[2], riderAsString[3], "");
			databaseHelper.getBicycleRiderDao().create(bicycleRider);
			return bicycleRider;
		} catch (NumberFormatException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage());
		}
		return null;
	}

	public void onClick(View v) {
		if (v instanceof TextView) {
			final TextView temp = (TextView) v;
			Integer checkedID = Integer.valueOf(temp.getText().toString());
			if (checkedIntegers.contains(checkedID)) {
				checkedIntegers.remove(checkedID);
				checkedViews.remove(temp);
				temp.setBackgroundColor(Color.TRANSPARENT);
				temp.setTextColor(Color.WHITE);

			} else {
				checkedIntegers.add(checkedID);
				checkedViews.add(temp);
				temp.setBackgroundColor(Color.LTGRAY);
				temp.setTextColor(Color.BLACK);
			}
		}
	}

	// TODO: make nicer and smoother
	public void onRowLayoutClick(TableRow tableRow, Object dragObject) {
		if (dragObject == null && checkedIntegers.size() == 0) {
			return;
		}

		if (hasToCreateNewGroup(tableRow)) {
			tableRow = raceFragment.getGroupFragment().createNewGroup(
					raceFragment.getGroupFragment().getTableRows()
							.indexOf(tableRow));
		}

		GroupTableRow groupTableRow = (GroupTableRow) tableRow;
		if (dragObject instanceof TextView) {
			TextView textView = (TextView) dragObject;
			((TableRow) textView.getParent()).removeView(textView);
			if (raceFragment.getGroupFragment().getField() != groupTableRow) {
				groupTableRow.addRider(Integer.valueOf(textView.getText()
						.toString()));
			}
		}

		else if (dragObject instanceof GroupTableRow) {
			final GroupTableRow draggedTableRow = (GroupTableRow) dragObject;
			if (groupTableRow == draggedTableRow) {
				return;
			}
			clearCheckedIntegers();
			if (groupTableRow == raceFragment.getGroupFragment().getField()) {
				draggedTableRow.removeAllViews();
				draggedTableRow.getGroup().getDriverNumbers().clear();
				raceFragment.getGroupFragment().removeEmptyTableRows();
			} else {
				while (draggedTableRow.getChildCount() > 2) {
					TextView temp = (TextView) draggedTableRow.getChildAt(2);
					draggedTableRow.removeView(temp);
					groupTableRow.addRider(Integer.valueOf(temp.getText()
							.toString()));
				}
			}
		} else {
			for (Integer i : checkedIntegers) {
				if (raceFragment.getGroupFragment().getField() == groupTableRow) {
					raceFragment.getGroupFragment().getDragListener()
							.handleDuplicates(i);
				} else {
					raceFragment.getGroupFragment().getDragListener()
							.addTextView(groupTableRow, i);
				}
			}
		}
		clearCheckedIntegers();
		raceFragment.getGroupFragment().removeEmptyTableRows();
	}

	private boolean hasToCreateNewGroup(TableRow row) {
		return raceFragment.getGroupFragment().getTableRows().indexOf(row) % 2 == 0;
	}

	public void clearCheckedIntegersOnclick(View v) {
		clearCheckedIntegers();
	}

	public void clearCheckedIntegers() {
		checkedIntegers.clear();
		for (TextView v : checkedViews) {
			v.setBackgroundColor(Color.TRANSPARENT);
			v.setTextColor(Color.WHITE);

		}
		checkedViews.clear();
	}

	// @Override
	// public void update(Observable observable, Object data) {
	// onRowLayoutClick((TableRow) data, null);
	// }

	private DatabaseHelper databaseHelper = null;

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

	public void ontestButtonClick(View v) {
		if (!raceFragment.isAdded()) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.changeLayout, raceFragment);
			fragmentTransaction.commit();
		}
	}

	public void ontestButtonClick1(View v) {
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

	public void ontestButtonClick2(View v) {
		clearCheckedIntegers();
		rankingFragment = rankingFragment == null ? new VirtualRankingFragment()
				: rankingFragment;
		if (!rankingFragment.isAdded()) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.changeLayout, rankingFragment);
			fragmentTransaction.commit();
		}
	}

	public void show(TextView v) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		FragmentDialog newFragment = new FragmentDialog(v);
		newFragment.show(ft, "dialog");

	}

	@Override
	public void update(Observable observable, Object data) {
		onRowLayoutClick((TableRow) data, null);
	}
}
// end Flo's Stuff
