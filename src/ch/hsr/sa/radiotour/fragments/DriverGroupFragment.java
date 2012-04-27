package ch.hsr.sa.radiotour.fragments;

import java.sql.SQLException;
import java.util.LinkedList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import com.j256.ormlite.table.TableUtils;

public class DriverGroupFragment extends Fragment {
	private GroupingDragListener dragListener;
	private DriverGroupClickListener clickListener;
	private LinkedList<TableRow> tableRows = new LinkedList<TableRow>();
	private TableLayout.LayoutParams standardParams;
	private TableRow.LayoutParams standardRowParams;
	private GroupTableRow field;
	private TableRow newGroupExample;
	private RuntimeExceptionDao<Group, Integer> databaseDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setDragListener();
		setOnClickListener();
		initializeTableRows();
	}

	private void initializeTableRows() {
		databaseDao = ((RadioTourActivity) getActivity()).getHelper()
				.getGroupDao();

		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup));
		tableRows.add((GroupTableRow) getView()
				.findViewById(R.id.tableRowField));
		field = (GroupTableRow) getView().findViewById(R.id.tableRowField);
		Group fieldGroup = new Group();
		fieldGroup.setField(true);
		field.setGroup(fieldGroup);

		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup2));
		newGroupExample = (TableRow) getView()
				.findViewById(R.id.tableRowGroup2);
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
				GroupTableRow tempRow = (GroupTableRow) createNewGroup(counter);
				tempRow.setGroup(g);
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

	public TableRow createNewGroup(int indexOf) {
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
		// final TextView test = new TextView(getActivity());
		// test.setOnLongClickListener(new OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View v) {
		// ClipData data = ClipData.newPlainText(test.getText(),
		// test.getText());
		// v.startDrag(data, new DragShadowBuilder(row), row, 0);
		// return true;
		// }
		// });
		// test.setText("Bestehende Gruppe");
		// test.setTextSize(40);
		// test.setLayoutParams(standardRowParams);
		// row.addView(test);
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
		test.setText("Neue Gruppe");
		row.addView(test);
		row.setLayoutParams(standardParams);
		row.setBackgroundResource(R.drawable.create_new_group);
		row.setOnDragListener(dragListener);
		row.setOnClickListener(clickListener);
		return row;
	}

	public void removeEmptyTableRows() {
		for (int i = 0; i < tableRows.size(); i++) {
			if (i % 2 == 1 && tableRows.get(i) != field) {
				GroupTableRow tempRow = (GroupTableRow) tableRows.get(i);
				if (tempRow.getChildCount() <= 2) {

					TableLayout tableLayout = (TableLayout) getView()
							.findViewById(R.id.groupTableLayout);
					tableLayout.removeView(tempRow);
					tableRows.remove(i);
					tableLayout.removeView(tableRows.get(i));
					tableRows.remove(i);
					--i;
				}
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
					+ (counter / 2));
		}
		prefix = getString(R.string.detached);
		for (counter = locationOfField + 2; counter < tableRows.size(); counter += 2) {
			((GroupTableRow) tableRows.get(counter)).changeDescription(prefix
					+ ((counter - locationOfField) / 2));
		}

	}

	private void syncToDb() {
		try {
			TableUtils.clearTable(((RadioTourActivity) getActivity())
					.getHelper().getConnectionSource(), Group.class);
		} catch (SQLException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
		((RadioTour) getActivity().getApplication()).getGroups().clear();
		for (int i = 1; i < tableRows.size(); i += 2) {
			Group gr = ((GroupTableRow) tableRows.get(i)).getGroup();
			gr.setOrderNumber(i / 2);
			databaseDao.create(gr);
			((RadioTour) getActivity().getApplication()).getGroups().add(gr);
		}
	}
}
