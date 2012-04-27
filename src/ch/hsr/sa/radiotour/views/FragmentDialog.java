package ch.hsr.sa.radiotour.views;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;

public class FragmentDialog extends DialogFragment {
	private NumberPicker hourPicker, minutePicker, secondPicker;
	private final TextView updateableTextView;

	public FragmentDialog(TextView txtView) {
		super();
		updateableTextView = txtView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(
				"WŠhlen des RŸckstandes der Gruppe relativ zur Spitze");
		View v = inflater.inflate(R.layout.fragment_dialog, container, false);

		hourPicker = ((NumberPicker) v.findViewById(R.id.hourPicker));
		hourPicker.setMinValue(0);
		hourPicker.setMaxValue(3);
		hourPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		hourPicker.setOnLongPressUpdateInterval(30);

		minutePicker = (NumberPicker) v.findViewById(R.id.minutePicker);
		minutePicker.setMinValue(0);
		minutePicker.setMaxValue(59);
		minutePicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		minutePicker.setOnLongPressUpdateInterval(30);

		secondPicker = (NumberPicker) v.findViewById(R.id.secondPicker);
		secondPicker.setMinValue(0);
		secondPicker.setMaxValue(59);
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
				updateableTextView.setText(getTimeString());
				dismiss();
			}
		});
		return v;
	}

	protected String getTimeString() {
		final NumberFormat formatter = new DecimalFormat("00");
		final StringBuffer buffer = new StringBuffer(
				formatter.format(hourPicker.getValue()));
		buffer.append(":");
		buffer.append(formatter.format(minutePicker.getValue()));
		buffer.append(":");
		buffer.append(formatter.format(secondPicker.getValue()));
		return buffer.toString();
	}

}
