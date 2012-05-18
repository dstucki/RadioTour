package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.fragments.DriverPickerFragment;

public class DriverPickerAdapter extends ArrayAdapter<Team> {
	private final Context context;
	private final DriverPickerFragment fragment;
	private final ArrayList<Team> teams;
	int[] ids = { R.id.startNr1, R.id.startNr2, R.id.startNr3, R.id.startNr4,
			R.id.startNr5, R.id.startNr6, R.id.startNr7, R.id.startNr8,
	// ...
	};

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

	public DriverPickerAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<Team> objects,
			DriverPickerFragment fragment) {
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
		BicycleRider rider;

		for (Integer driverNumber : team.getDriverNumbers()) {
			rider = ((RadioTour) context.getApplicationContext())
					.getRidersAsMap().get(driverNumber);
			if (rider != null) {
				TextView temp = (TextView) v.findViewById(ids[counter++]);

				if (rider.getRiderState() == RiderState.GIVEUP
						|| rider.getRiderState() == RiderState.NOT_STARTED) {
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

				temp.setText(Integer.toString(driverNumber));
				temp.setTextColor(rider.getRiderState().getTextColor());
				temp.setBackgroundColor((rider.getRiderState()
						.getBackgroundColor()));
			}
		}
		return v;
	}

	public void setOnClickListener(OnClickListener listener) {
		// TODO Auto-generated method stub

	}
}
