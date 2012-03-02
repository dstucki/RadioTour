package ch.hsr.sa.radiotour.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.BicycleRider;

public class DriverDetailFragment extends Fragment{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Test", "hello");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.driver_details, container, false);
		return view;
	}

	public void setDriver(BicycleRider item) {
		TextView viewStartNr = (TextView) getView().findViewById(R.id.detailsText);
		viewStartNr.setText(item.getStartNr()+"");
		TextView viewName = (TextView) getView().findViewById(R.id.driverName);
		viewName.setText(item.getName());
	}
}
