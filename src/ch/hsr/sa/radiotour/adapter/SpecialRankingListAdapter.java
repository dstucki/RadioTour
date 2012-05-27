package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.SpecialPointHolder;

public class SpecialRankingListAdapter extends
		ArrayAdapter<ArrayList<SpecialPointHolder>> {
	private final ArrayList<ArrayList<SpecialPointHolder>> pointHolder;

	public SpecialRankingListAdapter(Context context,
			ArrayList<ArrayList<SpecialPointHolder>> objects) {
		super(context, R.layout.textview_ranking_special_ranking,
				R.id.txtview_ridernr_special, objects);
		pointHolder = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.textview_ranking_special_ranking, null);

		ArrayList<SpecialPointHolder> points = pointHolder.get(position);
		int timeBoni = 0, pointBoni = 0;
		Rider rider = null;
		for (SpecialPointHolder point : points) {
			rider = point.getRider();
			timeBoni += point.getTimeBoni();
			pointBoni += point.getPointBoni();
		}
		((TextView) v.findViewById(R.id.txtview_ridernr_special)).setText(rider
				+ "");
		((TextView) v.findViewById(R.id.txtview_pointboni_special))
				.setText(pointBoni + "");
		((TextView) v.findViewById(R.id.txtview_timeboni_special))
				.setText(timeBoni + "");
		return v;
	}

}
