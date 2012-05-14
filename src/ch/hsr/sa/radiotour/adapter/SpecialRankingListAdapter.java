package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.SpecialPointHolder;

public class SpecialRankingListAdapter extends ArrayAdapter<SpecialPointHolder> {
	private final ArrayList<SpecialPointHolder> pointHolder;

	public SpecialRankingListAdapter(Context context,
			ArrayList<SpecialPointHolder> objects) {
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

		SpecialPointHolder point = pointHolder.get(position);
		((TextView) v.findViewById(R.id.txtview_ridernr_special)).setText(point
				.getRider() + "");
		((TextView) v.findViewById(R.id.txtview_pointboni_special))
				.setText(point.getPointBoni() + "");
		((TextView) v.findViewById(R.id.txtview_timeboni_special))
				.setText(point.getTimeBoni() + "");
		return v;
	}

}
