package ch.hsr.sa.radiotour.views;

import java.util.StringTokenizer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import ch.hsr.sa.radiotour.R;

public class FragmentDialog extends DialogFragment {
	private NumberPicker hourPicker, minutePicker, secondPicker;
	private final GroupTableRow selectedTableRow;
	private int hour, minute, second;

	public FragmentDialog(GroupTableRow txtView) {
		super();
		selectedTableRow = txtView;
		setTimeFromTextView();
	}

	private void setTimeFromTextView() {
		final String temp = selectedTableRow.getHandicapAsString();
		final StringTokenizer t = new StringTokenizer(temp, ":");
		hour = Integer.valueOf(t.nextToken());
		minute = Integer.valueOf(t.nextToken());
		second = Integer.valueOf(t.nextToken());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(
				"WŠhlen des RŸckstandes der Gruppe relativ zur Spitze");
		View v = inflater.inflate(R.layout.fragment_dialog, container, false);

		hourPicker = ((NumberPicker) v.findViewById(R.id.hourPicker));
		hourPicker.setMinValue(0);
		hourPicker.setMaxValue(12);
		hourPicker.setValue(hour);
		hourPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		hourPicker.setOnLongPressUpdateInterval(30);

		minutePicker = (NumberPicker) v.findViewById(R.id.minutePicker);
		minutePicker.setMinValue(0);
		minutePicker.setMaxValue(59);
		minutePicker.setValue(minute);
		minutePicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		minutePicker.setOnLongPressUpdateInterval(30);

		secondPicker = (NumberPicker) v.findViewById(R.id.secondPicker);
		secondPicker.setMinValue(0);
		secondPicker.setMaxValue(59);
		secondPicker.setValue(second);

		secondPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		secondPicker.setOnLongPressUpdateInterval(30);

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
				selectedTableRow.setHandicapTime(hourPicker.getValue(),
						minutePicker.getValue(), secondPicker.getValue());
				dismiss();
			}
		});
		return v;
	}

}
