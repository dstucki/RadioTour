package ch.hsr.sa.radiotour.adapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.fragments.HeaderFragment;
import ch.hsr.sa.radiotour.technicalservices.listener.GPSLocationListener;
import ch.hsr.sa.radiotour.utils.StringUtils;

public class PointsOfRaceAdapter extends ArrayAdapter<PointOfRace> implements
		Observer {
	private final List<PointOfRace> points;
	private PointOfRace actualPoint;

	public PointsOfRaceAdapter(Context context, int resource,
			int textViewResourceId, List<PointOfRace> objects) {
		super(context, resource, textViewResourceId, objects);
		points = objects;
		HeaderFragment.mGPS.addObserver(this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.point_item, null);
		v.setBackgroundColor(RiderState.NOT_STARTED.getBackgroundColor());
		PointOfRace point = points.get(position);
		if (point.isAlreadypassed() != HeaderFragment.mGPS.getDistanceInKm() > point
				.getDistance()) {
			point.setAlreadypassed(!point.isAlreadypassed());
			((RadioTourActivity) getContext()).getHelper().getPointOfRaceDao()
					.update(point);
			setActualPoint(point);
		}

		HeaderFragment.mGPS.getDistanceInKm();
		CheckBox chbx = (CheckBox) v.findViewById(R.id.chbx_passed);
		chbx.setChecked(point.isAlreadypassed());
		chbx.setEnabled(false);

		TextView altitude = (TextView) v.findViewById(R.id.txt_altitude);
		altitude.setText(point.getAltitude()
				+ getContext().getString(R.string.lb_m_suffix));

		TextView name = (TextView) v.findViewById(R.id.txt_point_name);
		name.setText(point.getName());
		TextView distance = (TextView) v.findViewById(R.id.txt_distance);
		distance.setText(Math.round(point.getDistance() * 10f) / 10f
				+ getContext().getString(R.string.lb_km_suffix));
		TextView date = (TextView) v.findViewById(R.id.txt_estimated_time);
		date.setText(StringUtils.getTimeWithoutSecondsAsString(point
				.getEstimatedDate()));

		if (point.isAlreadypassed()) {
			int color = RiderState.NOT_STARTED.getTextColor();
			altitude.setTextColor(color);
			name.setTextColor(color);
			distance.setTextColor(color);
			date.setTextColor(color);
		}

		return v;
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof GPSLocationListener) {
			notifyDataSetChanged();
		}

	}

	public PointOfRace getActualPoint() {
		return actualPoint;
	}

	public void setActualPoint(PointOfRace actualPoint) {
		this.actualPoint = actualPoint;
	}

}
