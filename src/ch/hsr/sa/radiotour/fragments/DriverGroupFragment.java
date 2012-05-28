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
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Group;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.fragments.controller.DriverGroupController;
import ch.hsr.sa.radiotour.technicalservices.connection.JsonSendingQueue;
import ch.hsr.sa.radiotour.technicalservices.listener.DriverGroupClickListener;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;
import ch.hsr.sa.radiotour.views.GroupTableRow;

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
		assignListener();
		setOnClickListener();
		initializeTableRows();
	}

	private void clearOldInformation() {
		tableRows.clear();
		driverTableRow.clear();
	}

	private void assignListener() {
		getView().findViewById(R.id.tableRowGroup).setOnDragListener(
				dragListener);
		getView().findViewById(R.id.tableRowField).setOnDragListener(
				dragListener);
		getView().findViewById(R.id.tableRowGroup2).setOnDragListener(
				dragListener);
	}

	private void initializeDriverRowMap() {
		for (Integer i : controller.getRiderNumbers()) {
			driverTableRow.put(i, field);
		}

	}

	private void initializeTableRows() {
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup));
		field = (GroupTableRow) getView().findViewById(R.id.tableRowField);
		tableRows.add(field);
		initializeDriverRowMap();
		field.changeDescription(getString(R.string.field));
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup2));
		standardParams = (TableLayout.LayoutParams) tableRows.get(0)
				.getLayoutParams();
		standardRowParams = (TableRow.LayoutParams) tableRows.get(0)
				.getChildAt(0).getLayoutParams();
		if (controller.getGroups().isEmpty()) {
			field.setGroup(controller.createFieldGroup());
		} else {
			alreadyGroupsHere();
		}
	}

	private void alreadyGroupsHere() {
		int counter = 0;
		for (Group g : controller.getGroups()) {
			if (!g.isField()) {
				GroupTableRow tempRow = createNewGroup(counter);
				tempRow.setGroup(g);
				for (int i : g.getDriverNumbers()) {
					field.removeRiderNr(i);
					tempRow.addRider(i);
					driverTableRow.put(i, tempRow);
				}
			} else {
				field.setGroup(g);
			}
			counter += 2;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.group_fragment, container, false);
		return view;
	}

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

	public void setDragListener(GroupingDragListener listener) {
		dragListener = listener;
	}

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

	private GroupTableRow createGroupRow() {
		final GroupTableRow row = new GroupTableRow(getActivity());
		row.setLayoutParams(standardParams);
		row.setBackgroundResource(R.drawable.working_group);
		row.setOnDragListener(dragListener);
		row.setOnClickListener(clickListener);
		return row;
	}

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

	private void assignNameToTableRow() {
		int locationOfField = tableRows.indexOf(field);
		int counter = 1;
		String prefix = getString(R.string.top);
		if (locationOfField > counter) {
			GroupTableRow temp = ((GroupTableRow) tableRows.get(counter));
			temp.getGroup().setLeader(true);
			temp.setTime(new Date(0, 0, 0, 0, 0, 0));
			temp.changeDescription(prefix);
		}

		if (locationOfField == counter) {
			field.getGroup().setLeader(true);
			field.setTime(new Date(0, 0, 0, 0, 0, 0));
		} else {
			field.getGroup().setLeader(false);
		}

		counter += 2;
		prefix = getString(R.string.follower);
		for (; counter < locationOfField; counter += 2) {
			GroupTableRow temp = ((GroupTableRow) tableRows.get(counter));
			temp.changeDescription(prefix + ((counter / 2) + 1));
			temp.getGroup().setLeader(false);

		}
		prefix = getString(R.string.detached);
		for (counter = locationOfField + 2; counter < tableRows.size(); counter += 2) {
			GroupTableRow temp = ((GroupTableRow) tableRows.get(counter));
			temp.changeDescription(prefix + ((counter - locationOfField) / 2));
			temp.getGroup().setLeader(false);
		}

	}

	private void syncToDb() {
		List<Group> groups = new LinkedList<Group>();
		for (int i = 1; i < tableRows.size(); i += 2) {
			GroupTableRow groupTableRow = (GroupTableRow) tableRows.get(i);
			groupTableRow.rearrangeTextViews();
			groups.add(groupTableRow.getGroup());
		}
		controller.saveGroups(groups);
	}

	private boolean hasToCreateNewGroup(TableRow row) {
		return tableRows.indexOf(row) % 2 == 0;
	}

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
				removeDriver(i);
				groupTableRow.addRider(i);
				driverTableRow.put(i, groupTableRow);

			}
		}
		removeEmptyTableRows();

	}

	private void selectOnATextView(TextView v, int i) {
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
			removeDriver(i);
			break;
		case R.id.not_started:
			newState = RiderState.NOT_STARTED;
			removeDriver(i);
			break;
		}
		RiderStageConnection riderStageConnection = ((RadioTour) getActivity()
				.getApplication()).getRiderStage(i);
		riderStageConnection.setRiderState(newState);
		controller.update(riderStageConnection);
		JsonSendingQueue.getInstance().addToQueue(riderStageConnection);
	}

	private void removeDriver(int i) {
		if (driverTableRow.get(i) != null) {
			driverTableRow.get(i).removeRiderNr(i);
		}
	}

}
