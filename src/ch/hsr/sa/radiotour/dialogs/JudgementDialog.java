package ch.hsr.sa.radiotour.dialogs;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.TextWatcherAdapter;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.fragments.SpecialRankingFragment;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

public class JudgementDialog extends DialogFragment {

	private final Judgement judgement;
	private EditText name, distance, nrOfWinners;
	private Spinner spinner;
	private ArrayAdapter<Stage> stageAdapter;
	private final SpecialRankingFragment fragment;
	private CheckBox time, point;
	private LinearLayout tableHolder;

	private final OnClickListener onSaveButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			save();
			dismiss();
		}
	};
	private final OnClickListener onAbortButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	private final OnItemSelectedListener stageListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			judgement.setStage(stageAdapter.getItem(position));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	};

	public JudgementDialog(SpecialRankingFragment fragment, Judgement judgement) {
		super();
		this.judgement = judgement;
		this.fragment = fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getString(R.string.new_judgement_dialog_title));
		View v = inflater.inflate(R.layout.textview_dialog, container, false);
		initializeViews(v);

		name.setText(judgement.getName());
		distance.setText(String.valueOf(judgement.getDistance()));
		nrOfWinners.setText(String.valueOf(judgement.getNrOfWinningDrivers()));
		time.setChecked(judgement.isTimeBoni());
		point.setChecked(judgement.isPointBoni());
		time.setOnClickListener(chbxk_clickListener);
		point.setOnClickListener(chbxk_clickListener);

		v.findViewById(R.id.dismissButton).setOnClickListener(
				onAbortButtonListener);
		v.findViewById(R.id.saveButton)
				.setOnClickListener(onSaveButtonListener);

		stageAdapter = new ArrayAdapter<Stage>(getActivity(),
				android.R.layout.simple_spinner_item);
		stageAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stageAdapter.addAll(DatabaseHelper.getHelper(getActivity())
				.getStageDao().queryForAll());
		spinner.setAdapter(stageAdapter);
		spinner.setSelection(stageAdapter.getPosition(judgement.getStage()));
		spinner.setOnItemSelectedListener(stageListener);

		handleTextViewTable();

		return v;
	}

	public void initializeViews(View v) {
		name = (EditText) v.findViewById(R.id.edtxt_judgement_name);
		distance = (EditText) v.findViewById(R.id.edtxt_judgement_distance);
		spinner = (Spinner) v.findViewById(R.id.spinner_stage);
		nrOfWinners = (EditText) v.findViewById(R.id.edtxt_nr_of_winners);
		nrOfWinners.addTextChangedListener(new TextWatcherAdapter() {

			@Override
			public void afterTextChanged(Editable s) {
				judgement
						.setNrOfWinningDrivers(getIntegerFromTextView(nrOfWinners));
				handleTextViewTable();
			}

		});
		time = (CheckBox) v.findViewById(R.id.chbx_timeboni);
		point = (CheckBox) v.findViewById(R.id.chbx_pointboni);
		tableHolder = (LinearLayout) v
				.findViewById(R.id.llayout_table_textview_holder);
	}

	private void save() {
		judgement.setName(name.getText().toString());
		ArrayList<Integer> timeboni = new ArrayList<Integer>();
		ArrayList<Integer> pointboni = new ArrayList<Integer>();

		LinearLayout ll;
		EditText temp;
		for (int i = 0; i < tableHolder.getChildCount(); i++) {
			ll = (LinearLayout) tableHolder.getChildAt(i);
			temp = (EditText) ll.findViewById(R.id.txtview_timeboni);
			timeboni.add(getIntegerFromTextView(temp));
			temp = (EditText) ll.findViewById(R.id.txtview_pointboni);
			pointboni.add(getIntegerFromTextView(temp));
		}

		if (judgement.isPointBoni()) {
			judgement.setPointBonis(pointboni);
		}
		if (judgement.isTimeBoni()) {
			judgement.setTimeBonis(timeboni);
		}
		fragment.newJudgementCreated(judgement);

	}

	private final OnClickListener chbxk_clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			judgement
					.setNrOfWinningDrivers(getIntegerFromTextView(nrOfWinners));
			judgement.setPointBoni(point.isChecked());
			judgement.setTimeBoni(time.isChecked());
			handleTextViewTable();

		}
	};

	private void handleTextViewTable() {
		if (!judgement.isTimeBoni() && !judgement.isPointBoni()) {
			tableHolder.removeAllViews();
			return;
		}

		setEnablingOfTextViews();

		if (tableHolder.getChildCount() == judgement.getNrOfWinningDrivers()) {
			return;
		}

		if (tableHolder.getChildCount() > judgement.getNrOfWinningDrivers()) {

			while (tableHolder.getChildCount() > judgement
					.getNrOfWinningDrivers()) {
				LinearLayout ll = (LinearLayout) tableHolder
						.getChildAt(judgement.getNrOfWinningDrivers());
				tableHolder.removeView(ll);
				tableHolder.invalidate();
			}
			return;
		}
		for (int i = tableHolder.getChildCount(); i < judgement
				.getNrOfWinningDrivers(); i++) {
			ViewGroup v = (ViewGroup) getActivity().getLayoutInflater()
					.inflate(R.layout.textview_table_special_ranking, null);
			EditText editText = (EditText) v
					.findViewById(R.id.txtview_timeboni);
			editText.setEnabled(judgement.isTimeBoni());
			editText.setText(String.valueOf(judgement.getTimeBonis().get(i)));
			editText = (EditText) v.findViewById(R.id.txtview_pointboni);
			editText.setEnabled(judgement.isPointBoni());
			editText.setText(String.valueOf(judgement.getPointBonis().get(i)));
			tableHolder.addView(v);
		}
	}

	private void setEnablingOfTextViews() {
		for (int i = 0; i < tableHolder.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) tableHolder.getChildAt(i);
			ll.findViewById(R.id.txtview_timeboni).setEnabled(
					judgement.isTimeBoni());
			ll.findViewById(R.id.txtview_pointboni).setEnabled(
					judgement.isPointBoni());
		}

	}

	private int getIntegerFromTextView(TextView t) {
		if (t.length() == 0) {
			return 0;
		}
		return Integer.valueOf(t.getText().toString());
	}

}