package ch.hsr.sa.radiotour.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.RiderPickerAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;
import ch.hsr.sa.radiotour.technicalservices.listener.RiderGroupClickListener;

public class RiderPickerFragment extends ListFragment implements
		OnClickListener {

	private RiderPickerAdapter adapter;
	private View footerView;
	private GroupingDragListener dragListener;
	private final RiderGroupClickListener clickListener = new RiderGroupClickListener();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new RiderPickerAdapter(getActivity(), R.layout.picklist_item,
				R.id.startNr1,
				((RadioTour) getActivity().getApplication()).getTeams(), this);
		footerView = getActivity().getLayoutInflater().inflate(
				R.layout.riderpicker_footer, null);
		getListView().addFooterView(footerView);
		assignListener();
		setListAdapter(adapter);

	}

	public void setDragListener(GroupingDragListener listener) {
		this.dragListener = listener;

	}

	public RiderPickerAdapter getAdapter() {
		return adapter;
	}

	private void assignListener() {
		footerView.findViewById(R.id.not_started).setOnDragListener(
				dragListener);
		footerView.findViewById(R.id.arzt).setOnDragListener(dragListener);
		footerView.findViewById(R.id.sturz).setOnDragListener(dragListener);
		footerView.findViewById(R.id.defekt).setOnDragListener(dragListener);
		footerView.findViewById(R.id.aufgabe).setOnDragListener(dragListener);

		clickListener.addObserver((RadioTourActivity) getActivity());

		footerView.findViewById(R.id.not_started).setOnClickListener(this);
		footerView.findViewById(R.id.not_started).setBackgroundColor(
				RiderState.NOT_STARTED.getBackgroundColor());

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
