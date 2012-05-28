package ch.hsr.sa.radiotour.fragments;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.domain.Group;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.fragments.controller.DriverGroupController;
import ch.hsr.sa.radiotour.technicalservices.listener.DriverGroupClickListener;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;
import ch.hsr.sa.radiotour.views.GroupTableRow;

/**
 * 
 * Class to display the area where the Riders are grouped
 * 
 */
public class DriverGroupFragment extends Fragment {
	private GroupingDragListener dragListener;
	private DriverGroupClickListener clickListener;
	private final LinkedList<TableRow> tableRows = new LinkedList<TableRow>();
	private final SparseArray<GroupTableRow> driverTableRow = new SparseArray<GroupTableRow>();
	private TableLayout.LayoutParams standardParams;
	private TableRow.LayoutParams standardRowParams;
	private GroupTableRow field;
	private DriverGroupController controller;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		controller = new DriverGroupController(this);
		clearOldInformation();
		assignDragListener();
		setOnClickListener();
		initializeTableRows();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.group_fragment, container, false);
		return view;
	}

	/**
	 * clear all the class intern information to avoid duplication
	 */
	private void clearOldInformation() {
		tableRows.clear();
		driverTableRow.clear();
	}

	/**
	 * Assign the {@link GroupingDragListener} object to the three predefined
	 * {@link TableRow} objects in the layout
	 */

	private void assignDragListener() {
		getView().findViewById(R.id.tableRowGroup).setOnDragListener(
				dragListener);
		getView().findViewById(R.id.tableRowField).setOnDragListener(
				dragListener);
		getView().findViewById(R.id.tableRowGroup2).setOnDragListener(
				dragListener);
	}

	/**
	 * Assign the {@link DriverGroupClickListener} object to the three
	 * predefined {@link TableRow} objects in the layout
	 */
	private void setOnClickListener() {
		clickListener = new DriverGroupClickListener();
		getView().findViewById(R.id.tableRowGroup).setOnClickListener(
				clickListener);
		getView().findViewById(R.id.tableRowField).setOnClickListener(
				clickListener);
		getView().findViewById(R.id.tableRowGroup2).setOnClickListener(
				clickListener);
		clickListener.addObserver((RadioTourActivity) getActivity());
	}

	/**
	 * Initial fill the driverTableRow {@link SparseArray} with the value of the
	 * field
	 */
	private void initializeDriverRowMap() {
		for (Integer i : controller.getRiderNumbers()) {
			driverTableRow.put(i, field);
		}

	}

	/**
	 * Initial add predefined Layout objects to the class intern datahandling
	 */
	private void initializeTableRows() {
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup));
		field = (GroupTableRow) getView().findViewById(R.id.tableRowField);
		tableRows.add(field);
		field.changeDescription(getString(R.string.field));
		field.setFragment(this);
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup2));
		standardParams = (TableLayout.LayoutParams) tableRows.get(0)
				.getLayoutParams();
		standardRowParams = (TableRow.LayoutParams) tableRows.get(0)
				.getChildAt(0).getLayoutParams();
		initializeDriverRowMap();
		if (controller.getGroups().isEmpty()) {
			field.setGroup(controller.createFieldGroup());
		} else {
			alreadyGroupsHere();
		}
	}

	/**
	 * Add for existing Groups a {@link GroupTableRow}
	 */
	private void alreadyGroupsHere() {
		int counter = 0;
		for (Group g : controller.getGroups()) {
			if (!g.isField()) {
				GroupTableRow tempRow = createNewGroup(counter);
				tempRow.setGroup(g);
				for (int i : g.getDriverNumbers()) {
					removeAndAddRiderNr(tempRow, i);
				}
			} else {
				field.setGroup(g);
			}
			counter += 2;
		}
	}

	/**
	 * set the {@link GroupingDragListener}
	 * 
	 * @param listener
	 */
	public void setDragListener(GroupingDragListener listener) {
		dragListener = listener;
	}

	/**
	 * Creates two {@link TableRow}, one that can accept Rider and one that acts
	 * as a layout to create a new group. Additionally it adds those two layouts
	 * tho the parent layouts
	 * 
	 * @param indexOf
	 *            where to create a new group
	 * @return the layout that accepts Rider
	 */
	private GroupTableRow createNewGroup(int indexOf) {
		TableLayout layout = (TableLayout) ((ScrollView) getView())
				.findViewById(R.id.groupTableLayout);
		GroupTableRow groupRow = createGroupRow();
		TableRow newGroupRow = createNewGroupRow();
		layout.addView(groupRow, indexOf);
		tableRows.add(indexOf, groupRow);
		layout.addView(newGroupRow, indexOf);
		tableRows.add(indexOf, newGroupRow);
		assignNameToTableRow();
		return groupRow;
	}

	/**
	 * Creates the {@link GroupTableRow} that accepts riders and returns it
	 * 
	 * @return {@link GroupTableRow}
	 */
	private GroupTableRow createGroupRow() {
		final GroupTableRow row = new GroupTableRow(getActivity());
		row.setLayoutParams(standardParams);
		row.setBackgroundResource(R.drawable.working_group);
		row.setOnDragListener(dragListener);
		row.setOnClickListener(clickListener);
		row.setFragment(this);
		return row;
	}

	/**
	 * Creates the {@link GroupTableRow} that is used to create new group
	 * 
	 * @return {@link TableRow}
	 */
	private TableRow createNewGroupRow() {
		TableRow row = new TableRow(getActivity());
		TextView test = new TextView(getActivity());
		test.setLayoutParams(standardRowParams);
		test.setTextSize(20);
		test.setText(getString(R.string.new_group));
		row.addView(test);
		row.setLayoutParams(standardParams);
		row.setBackgroundResource(R.drawable.create_new_group);
		row.setOnDragListener(dragListener);
		row.setOnClickListener(clickListener);
		return row;
	}

	/**
	 * remove TableRows that aren't used anymore. additionally it cleans up the
	 * descriptions and syncs the situation to the database
	 */
	private void removeEmptyTableRows() {
		GroupTableRow temp;
		for (int i = 1; i < tableRows.size(); i += 2) {
			temp = (GroupTableRow) tableRows.get(i);
			if (temp.getGroup().getDriverNumbers().isEmpty() && temp != field) {
				final TableLayout tableLayout = (TableLayout) getView()
						.findViewById(R.id.groupTableLayout);
				tableLayout.removeView(temp);
				tableRows.remove(i);
				tableLayout.removeView(tableRows.get(i));
				tableRows.remove(i);
				i -= 2;
			}

		}
		assignNameToTableRow();
		syncToDb();
	}

	/**
	 * sets the right descriptions to the {@link GroupTableRow} objects
	 */
	private void assignNameToTableRow() {
		int locationOfField = tableRows.indexOf(field);
		int counter = 1;
		String prefix = getString(R.string.top);
		GroupTableRow temp;
		if (locationOfField > counter) {
			temp = ((GroupTableRow) tableRows.get(counter));
			temp.getGroup().setLeader(true);
			temp.setTime(new Date(0, 0, 0, 0, 0, 0), false);
			temp.changeDescription(prefix);
		}

		if (locationOfField == counter) {
			field.getGroup().setLeader(true);
			field.setTime(new Date(0, 0, 0, 0, 0, 0), false);
		} else {
			field.getGroup().setLeader(false);
		}

		counter += 2;
		prefix = getString(R.string.follower);
		for (; counter < locationOfField; counter += 2) {
			temp = ((GroupTableRow) tableRows.get(counter));
			temp.changeDescription(prefix + ((counter / 2) + 1));
			temp.getGroup().setLeader(false);

		}
		prefix = getString(R.string.detached);
		for (counter = locationOfField + 2; counter < tableRows.size(); counter += 2) {
			temp = ((GroupTableRow) tableRows.get(counter));
			temp.changeDescription(prefix + ((counter - locationOfField) / 2));
			temp.getGroup().setLeader(false);
		}

	}

	/**
	 * syncs the situation to the db
	 */
	public void syncToDb() {
		List<Group> groups = new LinkedList<Group>();
		for (int i = 1; i < tableRows.size(); i += 2) {
			GroupTableRow groupTableRow = (GroupTableRow) tableRows.get(i);
			groupTableRow.rearrangeTextViews();
			groups.add(groupTableRow.getGroup());
		}
		controller.saveGroups(groups);
	}

	/**
	 * determines if the provided {@link TableRow} is one that accepts rider or
	 * one that is used to create a new group
	 * 
	 * @param row
	 * @return <code>true</code> if it has to create a new group, otherwise
	 *         <code>false</code>
	 */
	private boolean hasToCreateNewGroup(TableRow row) {
		return tableRows.indexOf(row) % 2 == 0;
	}

	/**
	 * handles the placement of the given ridernumbers
	 * 
	 * @param destination
	 *            {@link View} that the ridernumbers will go to
	 * @param riderNumbers
	 */
	public void moveDriverNr(View destination, Set<Integer> riderNumbers) {
		if (riderNumbers == null || riderNumbers.isEmpty()) {
			return;
		}
		GroupTableRow groupTableRow = null;

		if (destination instanceof TableRow) {
			if (hasToCreateNewGroup((TableRow) destination)) {
				destination = createNewGroup(tableRows.indexOf(destination));
			}
			groupTableRow = (GroupTableRow) destination;
		}

		TreeSet<Integer> modificationAvoider = new TreeSet<Integer>();
		modificationAvoider.addAll(riderNumbers);
		for (Integer i : modificationAvoider) {
			if (groupTableRow == null) {
				selectOnATextView((TextView) destination, i);
			} else {
				removeAndAddRiderNr(groupTableRow, i);

			}
		}
		removeEmptyTableRows();

	}

	/**
	 * removes the given ridernr from the old location and adds it on the new
	 * {@link GroupTableRow}
	 * 
	 * @param groupTableRow
	 *            the new {@link GroupTableRow}
	 * @param ridernr
	 *            the ridernr that has to be displaced
	 */
	private void removeAndAddRiderNr(GroupTableRow groupTableRow,
			Integer ridernr) {
		driverTableRow.get(ridernr).removeRiderNr(ridernr);
		groupTableRow.addRider(ridernr);
		driverTableRow.put(ridernr, groupTableRow);
	}

	/**
	 * handles a click or drag on a special event {@link TextView}
	 * 
	 * @param v
	 *            {@link TextView} that has been clicked or on that there was a
	 *            drop
	 * @param riderNr
	 *            the ridernr as for the special event occurs
	 * 
	 */
	private void selectOnATextView(TextView v, int riderNr) {
		RiderState newState = RiderState.ACTIV;
		switch (v.getId()) {
		case R.id.arzt:
			newState = RiderState.DOCTOR;
			break;
		case R.id.defekt:
			newState = RiderState.DEFECT;
			break;
		case R.id.sturz:
			newState = RiderState.FALL;
			break;
		case R.id.aufgabe:
			newState = RiderState.GIVEUP;
			driverTableRow.get(riderNr).removeRiderNr(riderNr);
			break;
		case R.id.not_started:
			newState = RiderState.NOT_STARTED;
			driverTableRow.get(riderNr).removeRiderNr(riderNr);
			break;
		}
		controller.setRiderState(riderNr, newState);
	}

}
