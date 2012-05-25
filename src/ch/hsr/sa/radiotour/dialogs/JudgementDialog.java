package ch.hsr.sa.radiotour.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.fragments.SpecialRakingFragment;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

public class JudgementDialog extends DialogFragment {

	private final Judgement judgement;
	private EditText name, distance;
	private Spinner spinner;
	private ArrayAdapter<Stage> stageAdapter;
	private final SpecialRakingFragment fragment;

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
			// TODO Auto-generated method stub

		}
	};

	public JudgementDialog(SpecialRakingFragment fragment, Judgement judgement) {
		super();
		this.judgement = judgement;
		this.fragment = fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getString(R.string.new_judgement_dialog_title));
		View v = inflater.inflate(R.layout.textview_dialog, container, false);
		name = (EditText) v.findViewById(R.id.edtxt_judgement_name);
		distance = (EditText) v.findViewById(R.id.edtxt_judgement_distance);
		spinner = (Spinner) v.findViewById(R.id.spinner_stage);

		name.setText(judgement.getName());
		distance.setText(judgement.getDistance() + "");

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

		return v;
	}

	private void save() {
		judgement.setName(name.getText().toString());
		fragment.nameChangedJudgement(judgement);
	}

}