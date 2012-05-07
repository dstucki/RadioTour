package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.sorting.RiderSortStrategy;
import ch.hsr.sa.radiotour.utils.StringUtils;

public class VirtualRankingAdapter extends ArrayAdapter<BicycleRider> {

	private final ArrayList<BicycleRider> riders;

	public VirtualRankingAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<BicycleRider> objects) {
		super(context, resource, textViewResourceId, objects);
		riders = objects;
	}

	public void sort(RiderSortStrategy strategy) {
		if (strategy.isAscending()) {
			Collections.sort(riders, Collections.reverseOrder(strategy));
		} else {
			Collections.sort(riders, strategy);
		}
		notifyDataSetChanged();
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
			temp.setText(rider.getTeamShort());

			temp = (TextView) v.findViewById(R.id.virtualRank);
			temp.setText((position + 1) + "");
			temp = (TextView) v.findViewById(R.id.timeBoni);
			temp.setText("toimplemnt");
			temp = (TextView) v.findViewById(R.id.timeOfficial);
			temp.setText(StringUtils.getTimeAsString(rider.getOfficial_time()));
			temp = (TextView) v.findViewById(R.id.handicapOfficial);
			temp.setText(StringUtils
					.getTimeAsString(new Date(0, 0, 0, 0, 0, 0)));
			temp = (TextView) v.findViewById(R.id.virtualDeficit);
			temp.setText(StringUtils.getTimeAsString(rider.getVirtual_deficit()));
		}

		return v;
	}
}
