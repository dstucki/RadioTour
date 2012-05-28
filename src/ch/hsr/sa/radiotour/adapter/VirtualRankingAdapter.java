package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.domain.sorting.RiderSortStrategy;
import ch.hsr.sa.radiotour.utils.StringUtils;

public class VirtualRankingAdapter extends ArrayAdapter<RiderStageConnection> {

	private final ArrayList<RiderStageConnection> connecters;
	private final Context context;
	private final List<RiderStageConnection> copyForCalculateVirtualRank;

	public VirtualRankingAdapter(Context context, int textViewResourceId,
			ArrayList<RiderStageConnection> objects) {
		super(context, textViewResourceId, objects);
		connecters = objects;
		copyForCalculateVirtualRank = new ArrayList<RiderStageConnection>();
		copyForCalculateVirtualRank.addAll(connecters);
		Collections.sort(copyForCalculateVirtualRank,
				new RiderSortStrategy.SortByVirtualDeficit());
		this.context = context;
	}

	public void sort(RiderSortStrategy strategy) {
		if (strategy.isAscending()) {
			Collections.sort(connecters, Collections.reverseOrder(strategy));
		} else {
			Collections.sort(connecters, strategy);
		}
		Collections.sort(copyForCalculateVirtualRank,
				new RiderSortStrategy.SortByVirtualDeficit());
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
		final RiderStageConnection conn = connecters.get(position);
		final Rider rider = conn.getRider();
		final RiderState state = conn.getRiderState();
		if (rider != null) {
			try {
				TextView temp = (TextView) v.findViewById(R.id.rang);
				temp.setText((position + 1) + "");
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.startnummer);
				temp.setText(rider.getStartNr() + "");
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.fahrername);
				temp.setText(rider.getName());
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.team);
				temp.setText(rider.getTeam().getShortName());
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.virtualRank);
				temp.setText(conn.getOfficialRank() + "");
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.timeBoni);
				temp.setText(StringUtils.getTimeAsString(new Date(0, 0, 0, 0,
						0, conn.getBonusTime())));
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.timeOfficial);
				temp.setText(StringUtils.getTimeAsString(conn.getOfficialTime()));
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.handicapOfficial);
				temp.setText(StringUtils.getTimeAsString(conn
						.getOfficialDeficit()));
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.virtualDeficit);
				temp.setText(StringUtils.getTimeAsString(conn
						.getVirtualDeficit()));
				temp.setTextColor(state.getTextColor());

				temp = (TextView) v.findViewById(R.id.land_text);
				temp.setText(rider.getCountry());
				temp.setTextColor(state.getTextColor());

				ImageView country = (ImageView) v.findViewById(R.id.land_image);
				country.setImageResource(context.getResources().getIdentifier(
						StringUtils.getCountryFlag(rider.getCountry()),
						"drawable", context.getPackageName()));

				v.setBackgroundColor(conn.getRiderState().getBackgroundColor());

			} catch (NullPointerException e) {
				Log.d(getClass().getSimpleName(), e.getMessage()
						+ " occured, can occur");
			}
		}

		return v;
	}
}
