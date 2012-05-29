package ch.hsr.sa.radiotour.adapter;

import java.util.List;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.domain.MaillotStageConnection;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.fragments.RiderPickerFragment;

public class RiderPickerAdapter extends ArrayAdapter<Team> {
	private final Context context;
	private final RiderPickerFragment fragment;
	private final List<Team> teams;
	int[] ids = { R.id.startNr1, R.id.startNr2, R.id.startNr3, R.id.startNr4,
			R.id.startNr5, R.id.startNr6, R.id.startNr7, R.id.startNr8, };

	private final OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			final TextView textView = (TextView) v;
			final int riderNr = Integer.valueOf(textView.getText().toString());
			if (!((RadioTourActivity) fragment.getActivity())
					.getCheckedIntegers().contains(riderNr)) {
				v.performClick();
			}
			ClipData data = ClipData.newPlainText(textView.getText(),
					textView.getText());
			v.startDrag(data, new DragShadowBuilder(v),
					((RadioTourActivity) fragment.getActivity())
							.getCheckedIntegers(), 0);
			notifyDataSetChanged();
			return true;
		}
	};

	public RiderPickerAdapter(Context context, int resource,
			int textViewResourceId, List<Team> objects,
			RiderPickerFragment fragment) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.fragment = fragment;
		teams = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.picklist_item, null);

		Team team = teams.get(position);
		int counter = 0;
		RiderStageConnection conn;
		MaillotStageConnection maillotCon;
		for (Rider rider : team.getDriverNumbers()) {
			conn = ((RadioTour) context.getApplicationContext())
					.getRiderStage(rider.getStartNr());

			if (conn != null) {
				TextView temp = (TextView) v.findViewById(ids[counter++]);
				temp.setCompoundDrawables(null, null, null, null);
				if (conn.getRiderState() == RiderState.GIVEUP
						|| conn.getRiderState() == RiderState.NOT_STARTED) {
					temp.setOnClickListener(null);
					temp.setOnLongClickListener(null);
					temp.setClickable(false);
					temp.setPaintFlags(temp.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);
				} else {
					temp.setClickable(true);
					temp.setOnClickListener((RadioTourActivity) context);
					temp.setOnLongClickListener(longClickListener);
				}
				temp.setText(Integer.toString(rider.getStartNr()));
				temp.setTextColor(conn.getRiderState().getTextColor());
				temp.setBackgroundColor((conn.getRiderState()
						.getBackgroundColor()));

				maillotCon = ((RadioTour) context.getApplicationContext())
						.getMaillotStage(rider.getStartNr());
				if (maillotCon != null) {
					Drawable draw = getMailloColor(maillotCon.getMaillot()
							.getColor());
					temp.setCompoundDrawablesWithIntrinsicBounds(null, null,
							draw, null);
				}
			}
		}
		return v;
	}

	/**
	 * Converts the Drawable of a Maillot to the given color representation
	 * 
	 * @param color
	 *            from {@link Maillot#getColor()}
	 * @return {@link Drawable} object represent the Maillot
	 */

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
