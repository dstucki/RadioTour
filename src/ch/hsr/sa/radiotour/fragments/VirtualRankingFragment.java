package ch.hsr.sa.radiotour.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.VirtualRankingAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;

public class VirtualRankingFragment extends ListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		VirtualRankingAdapter adapter = new VirtualRankingAdapter(
				getActivity(), R.layout.picklist_item, R.id.startNr1,
				((RadioTour) getActivity().getApplication()).getRiders());
		setListAdapter(adapter);

	}

}