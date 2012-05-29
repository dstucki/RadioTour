package ch.hsr.sa.radiotour.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.domain.MaillotStageConnection;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.fragments.AdminFragment;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

public class MaillotDialog extends DialogFragment {
	private View v;
	private final AdminFragment fragment;
	private Maillot maillot;
	private EditText maillotname;
	private EditText points;
	private EditText time;
	private EditText rider;
	private RadioGroup color;
	private Stage actualStage;
	private DatabaseHelper dbHelper;
	private MaillotStageConnection maillotStage;

	public MaillotDialog(AdminFragment fragment, Maillot maillot, Stage stage) {
		this.fragment = fragment;
		if (maillot != null && stage != null) {
			actualStage = stage;
			this.maillot = maillot;
		} else {
			this.maillot = new Maillot("", 0, 0, 0);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getString(R.string.addmaillot));
		v = inflater.inflate(R.layout.maillot_dialog, container, false);
		actualStage = ((RadioTour) getActivity().getApplicationContext())
				.getActualSelectedStage();
		dbHelper = DatabaseHelper.getHelper(getActivity()
				.getApplicationContext());

		maillotname = (EditText) v.findViewById(R.id.edittxt_maillot);
		points = (EditText) v.findViewById(R.id.edittxt_points);
		time = (EditText) v.findViewById(R.id.edittxt_time);
		rider = (EditText) v.findViewById(R.id.edittxt_rider);
		color = (RadioGroup) v.findViewById(R.id.rg_color);
		fillFields(maillot);

		v.findViewById(R.id.btn_save).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveMaillot();
				dismiss();
			}
		});
		v.findViewById(R.id.btn_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});

		return v;
	}

	private void fillFields(Maillot oldMaillot) {
		maillotname.setText(oldMaillot.getMaillot());
		points.setText(oldMaillot.getPoints() + "");
		time.setText(oldMaillot.getTime() + "");
		((RadioButton) v.findViewById(getButtonIDfromColor(oldMaillot
				.getColor()))).setChecked(true);

		Map<String, Object> constraints = new HashMap<String, Object>();
		constraints.put("etappe", actualStage);
		constraints.put("maillot", maillot);

		List<MaillotStageConnection> list = dbHelper.getMaillotStageDao()
				.queryForFieldValues(constraints);
		for (MaillotStageConnection maillotStageConnection : list) {
			final Rider riderObject = maillotStageConnection.getRider();
			rider.setText(riderObject == null ? "" : riderObject.getStartNr()
					+ "");
			this.maillotStage = maillotStageConnection;
		}
	}

	private void saveMaillot() {
		try {
			if (maillotname.length() != 0) {
				// Maillot name
				maillot.setMaillot(maillotname.getText().toString());

				// Points
				String pointstext = points.length() == 0 ? "0" : points
						.getText().toString();
				maillot.setPoints(Integer.valueOf(pointstext));

				// Time
				String timetext = time.length() == 0 ? "0" : time.getText()
						.toString();
				maillot.setTime(Long.valueOf(timetext));

				// Rider
				String ridernr = rider.getText().toString();

				// Color
				maillot.setColor(getColorFromButtonID(color
						.getCheckedRadioButtonId()));

				dbHelper.getMaillotRuntimeDao().createOrUpdate(maillot);

				if (ridernr.length() != 0) {
					Rider rider = ((RadioTour) getActivity()
							.getApplicationContext()).getRider(Integer
							.valueOf(ridernr));
					if (maillotStage == null) {
						maillotStage = new MaillotStageConnection(maillot,
								actualStage, rider);
						dbHelper.getMaillotStageDao().create(maillotStage);
					} else {
						maillotStage.setMaillot(maillot);
						maillotStage.setStage(actualStage);
						maillotStage.setRider(rider);
						dbHelper.getMaillotStageDao().update(maillotStage);
					}
				} else {
					Rider rider = new Rider();
					if (maillotStage == null) {
						maillotStage = new MaillotStageConnection(maillot,
								actualStage, rider);
						dbHelper.getMaillotStageDao().create(maillotStage);
					} else {
						maillotStage.setMaillot(maillot);
						maillotStage.setStage(actualStage);
						maillotStage.setRider(rider);
						dbHelper.getMaillotStageDao().update(maillotStage);
					}
				}
				fragment.newMaillotAdded(maillot);

			}
		} catch (NumberFormatException e) {
			Log.e(getClass().getSimpleName(), "no points added to maillot");
		}

	}

	private int getColorFromButtonID(int resid) {
		switch (resid) {
		case R.id.rdbtn_yellow:
			return Color.YELLOW;
		case R.id.rdbtn_green:
			return Color.GREEN;
		case R.id.rdbtn_red:
			return Color.RED;
		case R.id.rdbtn_reddot:
			return Color.BLACK;
		case R.id.rdbtn_pink:
			return Color.MAGENTA;
		default:
			return Color.WHITE;

		}
	}

	private int getButtonIDfromColor(int resid) {
		switch (resid) {
		case Color.YELLOW:
			return R.id.rdbtn_yellow;
		case Color.GREEN:
			return R.id.rdbtn_green;
		case Color.RED:
			return R.id.rdbtn_red;
		case Color.BLACK:
			return R.id.rdbtn_reddot;
		case Color.MAGENTA:
			return R.id.rdbtn_pink;
		default:
			return R.id.rdbtn_white;

		}
	}
}
