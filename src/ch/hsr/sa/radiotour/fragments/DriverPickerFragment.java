package ch.hsr.sa.radiotour.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.DriverPickerAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.technicalservices.listener.DriverGroupClickListener;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;

public class DriverPickerFragment extends ListFragment implements
		OnClickListener {

	private DriverPickerAdapter adapter;
	private View footerView;
	private GroupingDragListener dragListener;
	private final DriverGroupClickListener clickListener = new DriverGroupClickListener();

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
		this.dragListener = listener;

	}

	public DriverPickerAdapter getAdapter() {
		return adapter;
	}

	private void assignListener() {
		footerView.findViewById(R.id.arzt).setOnDragListener(dragListener);
		footerView.findViewById(R.id.sturz).setOnDragListener(dragListener);
		footerView.findViewById(R.id.defekt).setOnDragListener(dragListener);
		footerView.findViewById(R.id.aufgabe).setOnDragListener(dragListener);

		clickListener.addObserver((RadioTourActivity) getActivity());

		footerView.findViewById(R.id.arzt).setOnClickListener(this);
		footerView.findViewById(R.id.arzt).setBackgroundColor(
				RiderState.DOCTOR.getBackgroundColor());
		footerView.findViewById(R.id.sturz).setOnClickListener(this);
		footerView.findViewById(R.id.sturz).setBackgroundColor(
				RiderState.FALL.getBackgroundColor());

		footerView.findViewById(R.id.defekt).setOnClickListener(this);
		footerView.findViewById(R.id.defekt).setBackgroundColor(
				RiderState.DEFECT.getBackgroundColor());

		footerView.findViewById(R.id.aufgabe).setOnClickListener(this);
		footerView.findViewById(R.id.aufgabe).setBackgroundColor(
				RiderState.GIVEUP.getBackgroundColor());

	}

	@Override
	public void onClick(View v) {
		clickListener.onClick(v);
		adapter.notifyDataSetChanged();

	}
}
