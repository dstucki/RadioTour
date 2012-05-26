package ch.hsr.sa.radiotour.dialogs;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.fragments.AdminFragment;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

public class MaillotDialog extends DialogFragment {
	private View v;
	private final AdminFragment fragment;
	private Maillot maillot;

	public MaillotDialog(AdminFragment fragment) {
		this.fragment = fragment;
	}

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
				fragment.newMaillotAdded(maillot);
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

	private void saveMaillot() {
		try {
			maillot.setMaillot(((EditText) v.findViewById(R.id.edittxt_maillot))
					.getText().toString());
			maillot.setPoints(Integer.valueOf(((EditText) v
					.findViewById(R.id.edittxt_points)).getText().toString()));
			maillot.setTime(Long.valueOf(((EditText) v
					.findViewById(R.id.edittxt_time)).getText().toString()));
			maillot.setColor(getColorFromButtonID(((RadioGroup) v
					.findViewById(R.id.rg_color)).getCheckedRadioButtonId()));
			DatabaseHelper.getHelper(getActivity()).getMaillotRuntimeDao()
					.create(maillot);
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
}
