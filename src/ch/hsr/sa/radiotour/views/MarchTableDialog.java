package ch.hsr.sa.radiotour.views;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.PointsOfRaceAdapter;
import ch.hsr.sa.radiotour.domain.PointOfRace;

public class MarchTableDialog extends DialogFragment {
	View v;

	public MarchTableDialog() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Marschtabelle");
		v = inflater.inflate(R.layout.march_table_dialog, container, false);

		ListView lv = (ListView) v.findViewById(R.id.listView_march_table);
		lv.setAdapter(new PointsOfRaceAdapter(getActivity(),
				R.layout.point_item, R.id.txt_point_name,
				(ArrayList<PointOfRace>) ((RadioTourActivity) getActivity())
						.getHelper().getPointOfRaceDao().queryForAll()));
		return v;
	}
}