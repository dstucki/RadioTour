package ch.hsr.sa.radiotour.dialogs;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.VirtualRankingAdapter;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

public class EditRiderDialog extends DialogFragment {

	private static final String BIRTHDAY_TEMPLATE = "dd.MM.yyyy";
	private final RiderStageConnection connecter;
	private final BicycleRider rider;
	private final VirtualRankingAdapter adapter;
	private View v;

	public EditRiderDialog(RiderStageConnection connecter,
			VirtualRankingAdapter adapter) {
		super();
		this.connecter = connecter;
		rider = connecter.getRider();
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
			((EditText) v.findViewById(R.id.edittxt_day))
					.setText(new SimpleDateFormat(BIRTHDAY_TEMPLATE)
							.format(rider.getBirthday()));
			((EditText) v.findViewById(R.id.edittxt_note)).setText(rider
					.getNote());

			setStatiRadioButton();

		} catch (NullPointerException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
	}

	private void setStatiRadioButton() {
		RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.rg_status);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				connecter.setRiderState(getRiderState(checkedId));
			}
		});
		RadioButton temp = ((RadioButton) v.findViewById(getRadioID(connecter
				.getRiderState())));
		temp.setChecked(true);
	}

	private RiderState getRiderState(int checkedId) {
		switch (checkedId) {
		case R.id.rdbtn_in_race:
			return RiderState.ACTIV;
		case R.id.rdbtn_not_started:
			return RiderState.NOT_STARTED;
		case R.id.rdbtn_out:
			return RiderState.GIVEUP;
		default:
			return RiderState.ACTIV;
		}
	}

	private int getRadioID(RiderState state) {
		switch (state) {
		case NOT_STARTED:
			return R.id.rdbtn_not_started;
		case GIVEUP:
			return R.id.rdbtn_out;
		default:
			return R.id.rdbtn_in_race;
		}
	}

	private void save() {
		rider.setName(((EditText) v.findViewById(R.id.edittxt_name)).getText()
				.toString());
		rider.setNote(((EditText) v.findViewById(R.id.edittxt_note)).getText()
				.toString());
		rider.setTeam(((EditText) v.findViewById(R.id.edittxt_team)).getText()
				.toString());
		try {
			rider.setBirthday(new SimpleDateFormat(BIRTHDAY_TEMPLATE)
					.parse(((EditText) v.findViewById(R.id.edittxt_day))
							.getText().toString()));
		} catch (ParseException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
		DatabaseHelper.getHelper(getActivity()).getBicycleRiderDao()
				.update(rider);
		DatabaseHelper.getHelper(getActivity()).getRiderStageDao()
				.update(connecter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getString(R.string.edit_rider_dialog_title));
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