package ch.hsr.sa.radiotour.activities;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.fragments.DriverGroupFragment;
import ch.hsr.sa.radiotour.technicalservices.importer.CSVReader;

public class RadioTourActivity extends Activity implements Observer {
	private final TreeSet<Integer> checkedIntegers = new TreeSet<Integer>();
	private final HashSet<TextView> checkedViews = new HashSet<TextView>();
	private DriverGroupFragment groupFragment;

	public TreeSet<Integer> getCheckedIntegers() {
		return checkedIntegers;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.race_menu, menu);
		inflater.inflate(R.menu.special_menu, menu);
		inflater.inflate(R.menu.virtual_menu, menu);
		inflater.inflate(R.menu.driver_menu, menu);
		inflater.inflate(R.menu.admin_menu, menu);
		inflater.inflate(R.menu.log_menu, menu);
		return true;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CSVReader reader = new CSVReader(getResources().openRawResource(
				R.raw.startliste));
		for (String[] riderAsString : reader.readFile()) {
			((RadioTour) getApplication())
					.add(convertStringArrayToRider(riderAsString));
		}

		setContentView(R.layout.main);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		groupFragment = (DriverGroupFragment) getFragmentManager()
				.findFragmentById(R.id.detailFragment);
	}

	private BicycleRider convertStringArrayToRider(String[] riderAsString) {
		try {

			return new BicycleRider(Integer.valueOf(riderAsString[0]),
					riderAsString[1], riderAsString[2], riderAsString[3], "");
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

	private void onRowLayoutClick(View v) {
		TableRow row = (TableRow) v;
		if (hasToCreateNewGroup(row)) {
			row = groupFragment.createNewGroup(groupFragment.getTableRows()
					.indexOf(row));
		}
		for (Integer i : checkedIntegers) {
			if (groupFragment.getField() == (TableRow) v) {
				groupFragment.getDragListener().handleDuplicates(row, i);
			} else {
				groupFragment.getDragListener().addTextView(row, i);
			}
		}
		clearCheckedIntegers();
		groupFragment.removeEmptyTableRows();
	}

	private boolean hasToCreateNewGroup(TableRow row) {
		return groupFragment.getTableRows().indexOf(row) % 2 == 0;
	}

	public void clearCheckedIntegers() {
		checkedIntegers.clear();
		for (TextView v : checkedViews) {
			v.setBackgroundColor(Color.TRANSPARENT);
			v.setTextColor(Color.WHITE);

		}
		checkedViews.clear();
	}

	@Override
	public void update(Observable observable, Object data) {
		onRowLayoutClick((View) data);
	}
}