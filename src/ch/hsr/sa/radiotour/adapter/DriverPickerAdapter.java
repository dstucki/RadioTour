package ch.hsr.sa.radiotour.adapter;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.fragments.DriverPickerFragment;

public class DriverPickerAdapter extends ArrayAdapter<Team> {
	private final Context context;
	private final DriverPickerFragment fragment;
	private final ArrayList<Team> teams;
	int[] ids = { R.id.startNr1, R.id.startNr2, R.id.startNr3, R.id.startNr4,
			R.id.startNr5, R.id.startNr6, R.id.startNr7, R.id.startNr8 // ...
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
		for (Integer driverNumber : team.getDriverNumbers()) {

			TextView temp = (TextView) v
					.findViewById(ids[(driverNumber % 10) - 1]);

			temp.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					final TextView textView = (TextView) v;
					textView.performClick();
					((RadioTourActivity) fragment.getActivity())
							.getCheckedIntegers().add(
									Integer.valueOf(textView.getText()
											.toString()));
					StringBuffer clipDataBuffer = new StringBuffer();
					for (Integer i : ((RadioTourActivity) fragment
							.getActivity()).getCheckedIntegers()) {
						clipDataBuffer.append(i);
						clipDataBuffer.append(";");
					}
					ClipData data = ClipData.newPlainText(textView.getText(),
							textView.getText());

					v.startDrag(data, new DragShadowBuilder(v),
							((RadioTourActivity) fragment.getActivity())
									.getCheckedIntegers(), 0);
					return true;
				}
			});

			temp.setText(Integer.toString(driverNumber));
		}

		return v;
	}
}
