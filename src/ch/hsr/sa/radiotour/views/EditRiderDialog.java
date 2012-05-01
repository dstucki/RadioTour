package ch.hsr.sa.radiotour.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.VirtualRankingAdapter;
import ch.hsr.sa.radiotour.domain.BicycleRider;

public class EditRiderDialog extends DialogFragment {

	private static final String BIRTHDAY_TEMPLATE = "dd.MM.yyyy";
	private BicycleRider rider;
	private final VirtualRankingAdapter adapter;
	private View v;

	public EditRiderDialog(BicycleRider bicycleRider,
			VirtualRankingAdapter adapter) {
		super();
		rider = bicycleRider;
		this.adapter = adapter;
	}

	private void fillRiderInformation() {
		try {
			((EditText) v.findViewById(R.id.edittxt_startnr)).setText(String
					.valueOf(rider.getStartNr()));
			((EditText) v.findViewById(R.id.edittxt_name)).setText(rider
					.getName());
			((EditText) v.findViewById(R.id.edittxt_team)).setText(rider
					.getTeam());
			((EditText) v.findViewById(R.id.edittxt_team_short)).setText(rider
					.getTeamShort());
			((EditText) v.findViewById(R.id.edittxt_day))
					.setText(new SimpleDateFormat(BIRTHDAY_TEMPLATE)
							.format(rider.getBirthday()));
			((EditText) v.findViewById(R.id.edittxt_note)).setText(rider
					.getNote());
		} catch (NullPointerException e) {
			Log.e(getClass().getSimpleName(), "Fixme handle Exception");
		}
	}

	private void save() {
		rider.setName(((EditText) v.findViewById(R.id.edittxt_name)).getText()
				.toString());
		rider.setNote(((EditText) v.findViewById(R.id.edittxt_note)).getText()
				.toString());
		try {
			rider.setBirthday(new SimpleDateFormat(BIRTHDAY_TEMPLATE)
					.parse(((EditText) v.findViewById(R.id.edittxt_day))
							.getText().toString()));
		} catch (ParseException e) {
			Log.e(getClass().getSimpleName(), "Fixme handle Exception");
		}
		((RadioTourActivity) getActivity()).getHelper().getBicycleRiderDao()
				.createOrUpdate(rider);
	}

	public void setRider(BicycleRider rider) {
		this.rider = rider;
		fillRiderInformation();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Bearbeite Fahrer");
		v = inflater.inflate(R.layout.edit_rider_dialog, container, false);

		v.findViewById(R.id.btn_abort).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

					}
				});

		v.findViewById(R.id.btn_save).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				save();
				adapter.notifyDataSetChanged();
				dismiss();
			}
		});
		fillRiderInformation();
		return v;
	}

}