package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.utils.StringUtils;

public class PointsOfRaceAdapter extends ArrayAdapter<PointOfRace> {
	private final ArrayList<PointOfRace> points;
	int[] ids = { R.id.startNr1, R.id.startNr2, R.id.startNr3, R.id.startNr4,
			R.id.startNr5, R.id.startNr6, R.id.startNr7, R.id.startNr8,
			R.id.startNr9, R.id.startNr10, R.id.startNr11, R.id.startNr12 // ...
	};

	public PointsOfRaceAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<PointOfRace> objects) {
		super(context, resource, textViewResourceId, objects);
		points = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.point_item, null);

		PointOfRace point = points.get(position);
		TextView temp = (TextView) v.findViewById(R.id.txt_altitude);
		temp.setText(point.getAltitude() + " m");
		temp = (TextView) v.findViewById(R.id.txt_point_name);
		temp.setText(point.getName());
		temp = (TextView) v.findViewById(R.id.txt_distance);
		temp.setText(Math.round(point.getDistance() * 10f) / 10f + " km");
		temp = (TextView) v.findViewById(R.id.txt_estimated_time);
		temp.setText(StringUtils.getTimeAsString(point.getEstimatedDate()));

		return v;
	}

}
