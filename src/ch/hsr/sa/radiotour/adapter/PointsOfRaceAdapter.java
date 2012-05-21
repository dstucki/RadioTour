package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.utils.StringUtils;

public class PointsOfRaceAdapter extends ArrayAdapter<PointOfRace> {
	private final ArrayList<PointOfRace> points;

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

		CheckBox chbx = (CheckBox) v.findViewById(R.id.chbx_passed);
		chbx.setChecked(point.isAlreadypassed());
		TextView temp = (TextView) v.findViewById(R.id.txt_altitude);
		temp.setText(point.getAltitude() + " m");
		temp = (TextView) v.findViewById(R.id.txt_point_name);
		temp.setText(point.getName());
		temp = (TextView) v.findViewById(R.id.txt_distance);
		temp.setText(Math.round(point.getDistance() * 10f) / 10f + " km");
		temp = (TextView) v.findViewById(R.id.txt_estimated_time);
		temp.setText(StringUtils.getTimeWithoutSecondsAsString(point
				.getEstimatedDate()));

		return v;
	}

}
