package ch.hsr.sa.radiotour.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.technicalservices.listener.GroupingDragListener;

public class DriverDetailFragment extends Fragment {
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
		View view = inflater.inflate(R.layout.driver_details, container, false);
		return view;
	}

	public void setDriver(BicycleRider item) {
		TextView viewStartNr = (TextView) getView().findViewById(
				R.id.driverStartNr);
		viewStartNr.setText(item.getStartNr() + "");
		TextView viewName = (TextView) getView().findViewById(R.id.driverName);
		viewName.setText(item.getName());
		TextView viewTeam = (TextView) getView().findViewById(R.id.driverTeam);
		viewTeam.setText(item.getTeamName());
		TextView viewTeamShort = (TextView) getView().findViewById(
				R.id.driverTeamShort);
		viewTeamShort.setText(item.getTeamShort());
		TextView viewCountry = (TextView) getView().findViewById(
				R.id.driverCountry);
		viewCountry.setText(item.getCountry());
	}

	public void addDragListener(GroupingDragListener listner) {
		getView().setOnDragListener(listner);
		// TextView viewStartNr = (TextView) getView().findViewById(
		// R.id.driverStartNr);
		// viewStartNr.setOnDragListener(listner);
		// TextView viewName = (TextView)
		// getView().findViewById(R.id.driverName);
		// viewName.setOnDragListener(listner);
		// TextView viewTeam = (TextView)
		// getView().findViewById(R.id.driverTeam);
		// viewTeam.setOnDragListener(listner);
		// TextView viewTeamShort = (TextView) getView().findViewById(
		// R.id.driverTeamShort);
		// viewTeamShort.setOnDragListener(listner);
		// TextView viewCountry = (TextView) getView().findViewById(
		// R.id.driverCountry);
		// viewCountry.setOnDragListener(listner);
	}
}
