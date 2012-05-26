package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

public class MaillotsListAdapter extends ArrayAdapter<Maillot> {
	private ArrayList<Maillot> maillots;

	public MaillotsListAdapter(Context context, ArrayList<Maillot> objects) {
		super(context, R.layout.textview_ranking_special_ranking,
				R.id.txtview_ridernr_special, objects);
		maillots = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.maillot_item, null);

		final Maillot maillot = maillots.get(position);

		TextView name = (TextView) v.findViewById(R.id.maillot_name);
		name.setText(maillot.getMaillot());
		TextView points = (TextView) v.findViewById(R.id.maillot_points);
		points.setText(String.valueOf(maillot.getPoints()));

		TextView time = (TextView) v.findViewById(R.id.maillot_time);
		time.setText(String.valueOf(maillot.getTime()));

		ImageView image = (ImageView) v.findViewById(R.id.maillot_color);
		image.setImageDrawable(getMailloColor(maillot.getColor()));

		Button delete = (Button) v.findViewById(R.id.maillot_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHelper.getHelper(getContext()).getMaillotRuntimeDao()
						.delete(maillot);
				maillots.remove(maillot);
				notifyDataSetChanged();
			}
		});

		return v;
	}

	public void setMaillots(List<Maillot> maillots) {
		this.maillots = (ArrayList<Maillot>) maillots;
		notifyDataSetChanged();
	}

	private Drawable getMailloColor(int color) {
		switch (color) {
		case Color.YELLOW:
			return getContext().getResources().getDrawable(
					R.drawable.maillot_yellow);
		case Color.GREEN:
			return getContext().getResources().getDrawable(
					R.drawable.maillot_green);
		case Color.RED:
			return getContext().getResources().getDrawable(
					R.drawable.maillot_red);
		case Color.BLACK:
			return getContext().getResources().getDrawable(
					R.drawable.maillot_reddot);
		case Color.MAGENTA:
			return getContext().getResources().getDrawable(
					R.drawable.maillot_pink);
		default:
			return getContext().getResources().getDrawable(
					R.drawable.maillot_white);
		}
	}
}
