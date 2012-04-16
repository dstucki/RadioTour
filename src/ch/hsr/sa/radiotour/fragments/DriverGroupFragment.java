package ch.hsr.sa.radiotour.fragments;

import java.util.LinkedList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.technicalservices.listener.DriverGroupClickListener;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;

public class DriverGroupFragment extends Fragment {
	GroupingDragListener dragListener;
	DriverGroupClickListener clickListener;
	LinkedList<TableRow> tableRows = new LinkedList<TableRow>();
	TableLayout.LayoutParams standardParams;
	TableRow.LayoutParams standardRowParams;
	TableRow field;
	TableRow newGroupExample;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initializeTableRows();
		setDragListener();
		setOnClickListener();

	}

	private void initializeTableRows() {
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup));
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowField));
		field = (TableRow) getView().findViewById(R.id.tableRowField);
		tableRows.add((TableRow) getView().findViewById(R.id.tableRowGroup2));
		newGroupExample = (TableRow) getView()
				.findViewById(R.id.tableRowGroup2);
		standardParams = (TableLayout.LayoutParams) tableRows.get(0)
				.getLayoutParams();
		standardRowParams = (TableRow.LayoutParams) tableRows.get(0)
				.getChildAt(0).getLayoutParams();
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
		dragListener = new GroupingDragListener(getActivity());
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

	public void setField(TableRow field) {
		this.field = field;
	}

	public LinkedList<TableRow> getTableRows() {
		return tableRows;
	}

	public void setTableRows(LinkedList<TableRow> tableRows) {
		this.tableRows = tableRows;
	}

	public TableRow createNewGroup(int indexOf) {
		TableLayout layout = (TableLayout) getView();
		TableRow groupRow = createGroupRow();
		TableRow newGroupRow = createNewGroupRow();
		layout.addView(groupRow, indexOf);

		tableRows.add(indexOf, groupRow);
		layout.addView(newGroupRow, indexOf);
		tableRows.add(indexOf, newGroupRow);

		return groupRow;
	}

	private TableRow createGroupRow() {
		TableRow row = new TableRow(getActivity());
		TextView test = new TextView(getActivity());
		test.setText("Bestehende Gruppe");
		test.setTextSize(40);
		test.setLayoutParams(standardRowParams);
		row.addView(test);
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
		Log.d(getClass().getSimpleName(), standardParams.topMargin + "");
		row.setBackgroundResource(R.drawable.create_new_group);
		row.setOnDragListener(dragListener);
		row.setOnClickListener(clickListener);
		return row;
	}

	public void removeEmptyTableRows() {
		Log.d(getClass().getSimpleName(), "hihi");
		for (int i = 0; i < tableRows.size(); i++) {
			if (i % 2 == 1 && tableRows.get(i) != field) {
				TableRow tempRow = tableRows.get(i);
				if (tempRow.getChildCount() <= 1) {
					((TableLayout) getView()).removeView(tempRow);
					tableRows.remove(i);
					((TableLayout) getView()).removeView(tableRows.get(i));
					tableRows.remove(i);
					--i;
				}
			}

		}

	}
}
