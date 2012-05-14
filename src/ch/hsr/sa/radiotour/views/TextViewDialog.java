package ch.hsr.sa.radiotour.views;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.fragments.SpecialRakingFragment;

public class TextViewDialog extends DialogFragment {

	private final Judgement judgement;
	private EditText name;
	private final SpecialRakingFragment fragment;

	public TextViewDialog(SpecialRakingFragment fragment, Judgement judgement) {
		super();
		this.judgement = judgement;
		this.fragment = fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Name für Wertung eingeben");
		View v = inflater.inflate(R.layout.textview_dialog, container, false);
		name = (EditText) v.findViewById(R.id.edtxt_judgement_name);
		name.setText(judgement.getName());
		v.findViewById(R.id.dismissButton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

					}
				});

		v.findViewById(R.id.saveButton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						save();
						dismiss();
					}
				});
		return v;
	}

	private void save() {
		judgement.setName(name.getText().toString());
		fragment.nameChangedJudgement(judgement);
	}

}