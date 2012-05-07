package ch.hsr.sa.radiotour.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.DriverPickerAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;

public class DriverPickerFragment extends ListFragment {

	private DriverPickerAdapter adapter;
	private View footerView;
	private GroupingDragListener listener;

	private final TextView actualLayout = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new DriverPickerAdapter(getActivity(),
				R.layout.picklist_item, R.id.startNr1,
				((RadioTour) getActivity().getApplication()).getTeams(), this);
		footerView = getActivity().getLayoutInflater().inflate(
				R.layout.driverpicker_footer, null);
		getListView().addFooterView(footerView);
		assignListener();
		setListAdapter(adapter);

	}

	public void setOnClickListener(OnClickListener listener) {
		adapter.setOnClickListener(listener);
	}

	public void setDragListener(GroupingDragListener listener) {
		this.listener = listener;

	}

	public DriverPickerAdapter getAdapter() {
		return adapter;
	}

	private void assignListener() {
		footerView.findViewById(R.id.arzt).setOnDragListener(listener);
		footerView.findViewById(R.id.sturz).setOnDragListener(listener);
		footerView.findViewById(R.id.defekt).setOnDragListener(listener);
		footerView.findViewById(R.id.aufgabe).setOnDragListener(listener);
	}
}
