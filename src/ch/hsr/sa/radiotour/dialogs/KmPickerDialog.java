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
import ch.hsr.sa.radiotour.technicalservices.listener.GPSLocationListener;

public class KmPickerDialog extends DialogFragment {
	private final GPSLocationListener gpshandler;
	private EditText value;

	public KmPickerDialog(GPSLocationListener gps) {
		super();
		gpshandler = gps;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getResources().getString(R.string.hd_setdistance));
		View v = inflater.inflate(R.layout.km_picker_dialog, container, false);

		value = ((EditText) v.findViewById(R.id.racekmvalue));
		value.setText(Float.valueOf(gpshandler.getDistanceInKm()).toString());

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
				Float dist = new Float(value.getText().toString());
				gpshandler.setDistance(dist);
				gpshandler.updateLocation();
				dismiss();
			}
		});
		return v;
	}
}
