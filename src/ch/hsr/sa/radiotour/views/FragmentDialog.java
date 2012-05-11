package ch.hsr.sa.radiotour.views;

import java.util.Date;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.fragments.interfaces.TimePickerIF;

public class FragmentDialog extends DialogFragment {
	private final TimePickerIF selectedTableRow;
	private LinearLayout hourLayout, minuteLayout, seconLayout;
	private TextView selectedHour, selectedMinute, selectedSecond;
	private boolean useHour = false;

	public FragmentDialog(TimePickerIF txtView, boolean useHour) {
		super();
		selectedTableRow = txtView;
		this.useHour = useHour;

	}

	private void setTimeFromTextView() {
		Date date = selectedTableRow.getTime();
		if (date == null) {
			date = new Date(0, 0, 0, 0, 0, 0);
		}

		if (useHour) {
			selectedHour = findTextView(hourLayout, date.getHours());
			selectedHour.performClick();
		}
		selectedMinute = findTextView(minuteLayout, date.getMinutes());
		selectedMinute.performClick();

		selectedSecond = findTextView(seconLayout, date.getSeconds());
		selectedSecond.performClick();
	}

	private TextView findTextView(ViewGroup timeLayout, Integer valueOf) {
		ViewGroup temp = null;
		View temp3 = null;
		TextView returnValue = null;
		for (int i = 0; i < timeLayout.getChildCount(); i++) {
			temp = (ViewGroup) timeLayout.getChildAt(i);
			for (int k = 0; k < temp.getChildCount(); k++) {
				temp3 = temp.getChildAt(k);
				temp3.setOnClickListener(clickListener);
				temp3.setClickable(true);
				((TextView) temp3).setTextColor(Color.WHITE);
				if (Integer.valueOf(((TextView) temp3).getText().toString()) == valueOf) {
					returnValue = (TextView) temp3;
				}
			}

		}
		return returnValue;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(
				"Wählen des Rückstandes der Gruppe relativ zur Spitze");
		View v = inflater
				.inflate(R.layout.time_picker_dialog, container, false);

		LinearLayout holder = (LinearLayout) v
				.findViewById(R.id.llayout_number_table_holder);

		if (useHour) {
			hourLayout = (LinearLayout) inflater.inflate(
					R.layout.zero_to_fiftynine_table, null);
			holder.addView(hourLayout);
		}

		minuteLayout = (LinearLayout) inflater.inflate(
				R.layout.zero_to_fiftynine_table, null);
		seconLayout = (LinearLayout) inflater.inflate(
				R.layout.zero_to_fiftynine_table, null);

		holder.addView(minuteLayout);
		holder.addView(seconLayout);

		setTimeFromTextView();

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
				int hour = useHour ? getHour() : 0;
				selectedTableRow.setTime(new Date(0, 0, 0, hour, getMinutes(),
						getSeconds()));
				dismiss();
			}
		});
		return v;
	}

	private int getSeconds() {
		return Integer.valueOf(selectedSecond.getText().toString());
	}

	private int getHour() {
		return Integer.valueOf(selectedHour.getText().toString());
	}

	private int getMinutes() {
		return Integer.valueOf(selectedMinute.getText().toString());

	}

	final OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (useHour && v.getParent().getParent() == hourLayout) {
				selectedHour.setBackgroundColor(Color.TRANSPARENT);
				selectedHour.setTextColor(Color.WHITE);
				selectedHour = (TextView) v;
				selectedHour.setBackgroundColor(Color.LTGRAY);
				selectedHour.setTextColor(Color.BLACK);

			} else if (v.getParent().getParent() == minuteLayout) {
				selectedMinute.setBackgroundColor(Color.TRANSPARENT);
				selectedMinute.setTextColor(Color.WHITE);
				selectedMinute = (TextView) v;
				selectedMinute.setBackgroundColor(Color.LTGRAY);
				selectedMinute.setTextColor(Color.BLACK);

			} else if (v.getParent().getParent() == seconLayout) {
				selectedSecond.setBackgroundColor(Color.TRANSPARENT);
				selectedSecond.setTextColor(Color.WHITE);
				selectedSecond = (TextView) v;
				selectedSecond.setBackgroundColor(Color.LTGRAY);
				selectedSecond.setTextColor(Color.BLACK);
			}
		}
	};
}
