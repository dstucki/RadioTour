package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.BicycleRider;

public class DriverListAdapter extends ArrayAdapter<BicycleRider> {

	private final ArrayList<BicycleRider> riders;

	public DriverListAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<BicycleRider> objects) {
		super(context, resource, textViewResourceId, objects);
		riders = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item, null);
		}

		BicycleRider rider = riders.get(position);
		if (rider != null) {
			TextView country = (TextView) v.findViewById(R.id.getCountry);
			country.setText(rider.getCountry());
			TextView team = (TextView) v.findViewById(R.id.getTeam);
			team.setText(rider.getTeamName());
			TextView name = (TextView) v.findViewById(R.id.getName);
			name.setText(rider.getName());

			TextView startNr = (TextView) v.findViewById(R.id.getStartNr);
			startNr.setText(rider.getStartNr() + "");

		}

		return v;
	}

}
