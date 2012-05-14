package ch.hsr.sa.radiotour.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Stage;

public class AdminFragment extends Fragment {

	private ArrayAdapter<Stage> adapterForStageSpinner;
	private RadioTour app;

	private final OnItemSelectedListener stageListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			Stage actualStage = adapterForStageSpinner.getItem(position);
			app.setActualSelectedStage(actualStage);
			((RadioTourActivity) getActivity()).updateStage(actualStage);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.admin_fragment, container, false);
		app = (RadioTour) getActivity().getApplication();
		final Spinner stageSpinner = (Spinner) view.findViewById(R.id.spinner1);
		adapterForStageSpinner = new ArrayAdapter<Stage>(getActivity(),
				android.R.layout.simple_spinner_item);
		adapterForStageSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterForStageSpinner.addAll(((RadioTourActivity) getActivity())
				.getHelper().getStageDao().queryForAll());
		stageSpinner.setAdapter(adapterForStageSpinner);
		stageSpinner.setSelection(app.getActualSelectedStage().getId() - 1);

		stageSpinner.setOnItemSelectedListener(stageListener);
		return view;
	}
}