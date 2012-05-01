package ch.hsr.sa.radiotour.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View.OnClickListener;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.DriverPickerAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;

public class DriverPickerFragment extends ListFragment {

	private DriverPickerAdapter adapter;

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
		getListView().addFooterView(
				getActivity().getLayoutInflater().inflate(
						R.layout.driverpicker_footer, null));
		setListAdapter(adapter);

	}

	public void setOnClickListener(OnClickListener listener) {
		adapter.setOnClickListener(listener);
	}

}
