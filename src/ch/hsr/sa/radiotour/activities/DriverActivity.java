package ch.hsr.sa.radiotour.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

public class DriverActivity extends Fragment {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(this.getClass().getSimpleName(), "I'm Here");
	}
}
