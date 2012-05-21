package ch.hsr.sa.radiotour.views;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.domain.Maillot;

public class MaillotDialog extends DialogFragment {
	private View v;
	private Maillot maillot;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		maillot = new Maillot();
		getDialog().setTitle(getString(R.string.addmaillot));
		v = inflater.inflate(R.layout.maillot_dialog, container, false);

		v.findViewById(R.id.btn_save).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveMaillot();
				dismiss();
			}
		});

		return v;
	}

	private void saveMaillot() {
		maillot.setMaillot(((EditText) v.findViewById(R.id.edittxt_maillot))
				.getText().toString());
		maillot.setPoints(Integer.valueOf(((EditText) v
				.findViewById(R.id.edittxt_points)).getText().toString()));
		maillot.setTime(Long.valueOf(((EditText) v
				.findViewById(R.id.edittxt_time)).getText().toString()));
		// TODO: Color is not set correctly here
		maillot.setColor(((RadioGroup) v.findViewById(R.id.rg_color))
				.getCheckedRadioButtonId());
		((RadioTourActivity) getActivity()).getHelper().getMaillotRuntimeDao()
				.create(maillot);

	}
}
