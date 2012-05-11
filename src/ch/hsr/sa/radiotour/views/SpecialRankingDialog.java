package ch.hsr.sa.radiotour.views;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.fragments.SpecialRakingFragment;

public class SpecialRankingDialog extends DialogFragment {
	private View v;
	private SpecialRanking specialRanking;
	private EditText name, nrOfWinner;
	private CheckBox time, point;
	private LinearLayout tableHolder;
	private final SpecialRakingFragment fragment;

	private final OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			specialRanking.setNrOfWinningDrivers(Integer.valueOf(nrOfWinner
					.getText().toString()));
			specialRanking.setPointBoni(point.isChecked());
			specialRanking.setTimeBoni(time.isChecked());
			handleTextViewTable();

		}
	};

	public SpecialRankingDialog(SpecialRakingFragment fragment,
			SpecialRanking specialRanking) {
		this.specialRanking = specialRanking;
		this.fragment = fragment;
		if (specialRanking == null) {
			this.specialRanking = createStandardSpecialRanking();
		}

	}

	private void fillInformation() {
		name = (EditText) v.findViewById(R.id.edtxt_name_special_ranking);
		name.setText(specialRanking.getName());
		nrOfWinner = (EditText) v.findViewById(R.id.edtxt_nr_of_winners);
		nrOfWinner.setText(specialRanking.getNrOfWinningDrivers() + "");
		nrOfWinner.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (v == nrOfWinner && !hasFocus) {
					specialRanking.setNrOfWinningDrivers(Integer
							.valueOf(nrOfWinner.getText().toString()));
					handleTextViewTable();
				}

			}
		});
		time = (CheckBox) v.findViewById(R.id.chbx_timeboni);
		time.setChecked(specialRanking.isTimeBoni());
		time.setOnClickListener(listener);
		point = (CheckBox) v.findViewById(R.id.chbx_pointboni);
		point.setChecked(specialRanking.isPointBoni());
		point.setOnClickListener(listener);

		handleTextViewTable();
	}

	public void handleTextViewTable() {
		if (!specialRanking.isTimeBoni() && !specialRanking.isPointBoni()) {
			tableHolder.removeAllViews();
			return;
		}

		setEnablingOfTextViews();

		if (tableHolder.getChildCount() == specialRanking
				.getNrOfWinningDrivers()) {
			return;
		}

		if (tableHolder.getChildCount() > specialRanking
				.getNrOfWinningDrivers()) {

			while (tableHolder.getChildCount() > specialRanking
					.getNrOfWinningDrivers()) {
				LinearLayout ll = (LinearLayout) tableHolder
						.getChildAt(specialRanking.getNrOfWinningDrivers());
				tableHolder.removeView(ll);
				tableHolder.invalidate();
			}
			return;
		}
		for (int i = tableHolder.getChildCount(); i < specialRanking
				.getNrOfWinningDrivers(); i++) {
			ViewGroup v = (ViewGroup) getActivity().getLayoutInflater()
					.inflate(R.layout.textview_table_special_ranking, null);
			EditText editText = (EditText) v
					.findViewById(R.id.txtview_timeboni);
			editText.setEnabled(specialRanking.isTimeBoni());
			editText.setText(specialRanking.getTimeBonis().get(i) + "");
			editText = (EditText) v.findViewById(R.id.txtview_pointboni);
			editText.setEnabled(specialRanking.isPointBoni());
			editText.setText(specialRanking.getPointBonis().get(i) + "");

			tableHolder.addView(v);
		}
	}

	public void setEnablingOfTextViews() {
		for (int i = 0; i < tableHolder.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) tableHolder.getChildAt(i);
			ll.findViewById(R.id.txtview_timeboni).setEnabled(
					specialRanking.isTimeBoni());
			ll.findViewById(R.id.txtview_pointboni).setEnabled(
					specialRanking.isPointBoni());
		}
	}

	private SpecialRanking createStandardSpecialRanking() {
		ArrayList<Integer> timeBonis = new ArrayList<Integer>();
		timeBonis.add(5);
		timeBonis.add(3);
		timeBonis.add(1);
		ArrayList<Integer> pointBonis = new ArrayList<Integer>();
		pointBonis.add(3);
		pointBonis.add(2);
		pointBonis.add(1);
		return new SpecialRanking("Test Klassement", 3, true, true, timeBonis,
				pointBonis);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Marschtabelle");
		v = inflater.inflate(R.layout.special_ranking_dialog_fragment,
				container, false);
		tableHolder = (LinearLayout) v
				.findViewById(R.id.llayout_table_textview_holder);

		Button button = (Button) v.findViewById(R.id.dismissButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		button = (Button) v.findViewById(R.id.saveButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				save();

				fragment.setSpecialRankingFromDialog(specialRanking);
				dismiss();
			}
		});

		fillInformation();
		return v;
	}

	private void save() {
		specialRanking.setName(name.getText().toString());
		ArrayList<Integer> timeboni = new ArrayList<Integer>();
		ArrayList<Integer> pointboni = new ArrayList<Integer>();

		for (int i = 0; i < tableHolder.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) tableHolder.getChildAt(i);

			EditText temp = (EditText) ll.findViewById(R.id.txtview_timeboni);
			if (temp.getText().toString().equals("")) {
				timeboni.add(0);
			} else {
				timeboni.add(Integer.valueOf(temp.getText().toString()));
			}
			temp = (EditText) ll.findViewById(R.id.txtview_pointboni);
			if (temp.getText().toString().equals("")) {
				pointboni.add(0);
			} else {
				pointboni.add(Integer.valueOf(temp.getText().toString()));
			}
		}
		if (specialRanking.isPointBoni()) {
			specialRanking.setPointBonis(pointboni);
		}
		if (specialRanking.isTimeBoni()) {
			specialRanking.setTimeBonis(timeboni);
		}
	}

}
