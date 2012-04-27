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

public class VirtualRankingAdapter extends ArrayAdapter<BicycleRider> {

	private final ArrayList<BicycleRider> riders;

	public VirtualRankingAdapter(Context context, int resource,
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
			v = vi.inflate(R.layout.virtual_ranking_item, null);
		}
		final BicycleRider rider = riders.get(position);
		if (rider != null) {
			TextView temp = (TextView) v.findViewById(R.id.rang);
			temp.setText((position + 1) + "");
			temp = (TextView) v.findViewById(R.id.startnummer);
			temp.setText(rider.getStartNr() + "");
			temp = (TextView) v.findViewById(R.id.fahrername);
			temp.setText(rider.getName());
			temp = (TextView) v.findViewById(R.id.team);
			temp.setText(rider.getTeam());
			temp = (TextView) v.findViewById(R.id.land);
			temp.setText(rider.getTeamShort());
			temp = (TextView) v.findViewById(R.id.gruppen);
			temp.setText("Gruppen-Nr");
			temp = (TextView) v.findViewById(R.id.virtualRank);
			temp.setText((position + 1) + "");
			temp = (TextView) v.findViewById(R.id.timeBoni);
			temp.setText("01:00:01");
			temp = (TextView) v.findViewById(R.id.timeOfficial);
			temp.setText("01:00:01");
			temp = (TextView) v.findViewById(R.id.handicapOfficial);
			temp.setText("01:00:01");
		}

		return v;
	}
}