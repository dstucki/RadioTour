package ch.hsr.sa.radiotour.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;

public class RaceFragment extends Fragment {

	private DriverPickerFragment driverPicker;
	private DriverGroupFragment groupFragment;
	private GroupingDragListener listener;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		driverPicker = new DriverPickerFragment();
		groupFragment = new DriverGroupFragment();
		listener = new GroupingDragListener(getActivity(), driverPicker);
		driverPicker.setDragListener(listener);
		groupFragment.setDragListener(listener);

		transaction.replace(R.id.pickerFragmentHolder, driverPicker);
		transaction.replace(R.id.groupFragmentHolder, groupFragment);
		transaction.commit();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.race_layout, container, false);
		return view;
	}

	public DriverPickerFragment getDriverPicker() {
		return driverPicker;
	}

	public DriverGroupFragment getGroupFragment() {
		return groupFragment;
	}

}
