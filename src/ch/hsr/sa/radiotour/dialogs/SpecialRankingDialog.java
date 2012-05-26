package ch.hsr.sa.radiotour.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.fragments.SpecialRankingFragment;

public class SpecialRankingDialog extends DialogFragment {
	private View v;
	private SpecialRanking specialRanking;
	private EditText name;
	private final SpecialRankingFragment fragment;
	private boolean hasToSetName = false;

	public SpecialRankingDialog(SpecialRankingFragment fragment,
			SpecialRanking specialRanking) {
		this.specialRanking = specialRanking;
		this.fragment = fragment;
		if (specialRanking == null) {
			this.specialRanking = createStandardSpecialRanking();
			hasToSetName = true;
		}

	}

	private void fillInformation() {
		name = (EditText) v.findViewById(R.id.edtxt_name_special_ranking);
		name.setText(specialRanking.getName());
		handleTextViewTable();
	}

	public void handleTextViewTable() {
	}

	public void setEnablingOfTextViews() {
	}

	private SpecialRanking createStandardSpecialRanking() {
		return new SpecialRanking();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getResources().getString(R.string.hd_marchtable));
		v = inflater.inflate(R.layout.special_ranking_dialog_fragment,
				container, false);

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
		if (hasToSetName) {
			specialRanking
					.setName(getResources().getString(R.string.lb_newkla));
		}
		fillInformation();
		return v;
	}

	private void save() {
		specialRanking.setName(name.getText().toString());
	}

}
