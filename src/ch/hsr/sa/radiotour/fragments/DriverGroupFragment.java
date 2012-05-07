package ch.hsr.sa.radiotour.fragments;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

import android.app.Fragment;
import android.os.Bundle;
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
import ch.hsr.sa.radiotour.technicalservices.listener.DriverGroupClickListener;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;
import ch.hsr.sa.radiotour.views.GroupTableRow;

import com.j256.ormlite.dao.RuntimeExceptionDao;

public class DriverGroupFragment extends Fragment {
	private GroupingDragListener dragListener;
	private DriverGroupClickListener clickListener;
	private LinkedList<TableRow> tableRows = new LinkedList<TableRow>();
	private final HashMap<Integer, GroupTableRow> driverTableRow = new HashMap<Integer, GroupTableRow>();
	private TableLayout.LayoutParams standardParams;
	private TableRow.LayoutParams standardRowParams;
	private GroupTableRow field;
	private RuntimeExceptionDao<Group, Integer> groupDatabaseDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tableRows.clear();
		driverTableRow.clear();
		groupDatabaseDao = ((RadioTourActivity) getActivity()).getHelper()
				.getGroupDao();
		((RadioTour) getActivity().getApplication()).getGroups().clear();
		((RadioTour) getActivity().getApplication()).getGroups().addAll(
				groupDatabaseDao.queryForAll());

		setDragListener();
		setOnClickListener();
		initializeDriverRowMap();
		initializeTableRows();
	}

	private void initializeDriverRowMap() {
		for (Integer i : ((RadioTour) getActivity().getApplication())
				.getRiderNumbers()) {
			driverTableRow.put(i, null);
		}

	}

	private void initializeTableRows() {

		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup));
		tableRows.add((GroupTableRow) getView()
				.findViewById(R.id.tableRowField));
		field = (GroupTableRow) getView().findViewById(R.id.tableRowField);
		field.changeDescription(getString(R.string.field));
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup2));
		standardParams = (TableLayout.LayoutParams) tableRows.get(0)
				.getLayoutParams();
		standardRowParams = (TableRow.LayoutParams) tableRows.get(0)
				.getChildAt(0).getLayoutParams();

		if (((RadioTour) getActivity().getApplication()).getGroups().isEmpty()) {
			return;
		}
		alreadyGroupsHere();
	}

	private void alreadyGroupsHere() {
		int counter = 0;
		for (Group g : ((RadioTour) getActivity().getApplication()).getGroups()) {
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

	public GroupingDragListener getDragListener() {
		return dragListener;
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

	private void setDragListener() {
		dragListener = new GroupingDragListener(getActivity(), this);
		getView().findViewById(R.id.tableRowGroup).setOnDragListener(
				dragListener);
		getView().findViewById(R.id.tableRowField).setOnDragListener(
				dragListener);
		getView().findViewById(R.id.tableRowGroup2).setOnDragListener(
				dragListener);

	}

	public TableRow getField() {
		return field;
	}

	public void setField(GroupTableRow field) {
		this.field = field;
	}

	public LinkedList<TableRow> getTableRows() {
		return tableRows;
	}

	public void setTableRows(LinkedList<TableRow> tableRows) {
		this.tableRows = tableRows;
	}

	public GroupTableRow createNewGroup(int indexOf) {
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
				groupDatabaseDao.delete(temp.getGroup());
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
			((GroupTableRow) tableRows.get(counter)).changeDescription(prefix);
		}
		counter += 2;
		prefix = getString(R.string.follower);
		for (; counter < locationOfField; counter += 2) {
			((GroupTableRow) tableRows.get(counter)).changeDescription(prefix
					+ ((counter / 2) + 1));
		}
		prefix = getString(R.string.detached);
		for (counter = locationOfField + 2; counter < tableRows.size(); counter += 2) {
			((GroupTableRow) tableRows.get(counter)).changeDescription(prefix
					+ ((counter - locationOfField) / 2));
		}

	}

	private void syncToDb() {
		for (int i = 1; i < tableRows.size(); i += 2) {
			Group gr = ((GroupTableRow) tableRows.get(i)).getGroup();
			if ((i / 2) != gr.getOrderNumber()) {
				gr.setOrderNumber(i / 2);
				groupDatabaseDao.createOrUpdate(gr);
			}
		}
	}

	private boolean hasToCreateNewGroup(TableRow row) {
		return tableRows.indexOf(row) % 2 == 0;
	}

	public void moveDriverNr(TableRow destination, TreeSet<Integer> riderNumbers) {
		if (riderNumbers == null || riderNumbers.isEmpty()) {
			return;
		}
		if (hasToCreateNewGroup(destination)) {
			destination = createNewGroup(tableRows.indexOf(destination));
		}
		final GroupTableRow groupTableRow = (GroupTableRow) destination;
		TreeSet<Integer> modificationAvoider = new TreeSet<Integer>();
		modificationAvoider.addAll(riderNumbers);
		for (Integer i : modificationAvoider) {
			if (driverTableRow.get(i) != null) {
				driverTableRow.get(i).removeRiderNr(i);
			}
			groupTableRow.addRider(i);
			driverTableRow.put(i, groupTableRow);
		}
		groupDatabaseDao.createOrUpdate(groupTableRow.getGroup());
		removeEmptyTableRows();
	}
}
