package ch.hsr.sa.radiotour.technicalservices.importer;

import android.os.AsyncTask;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;

public class Importer extends AsyncTask<RadioTourActivity, Integer, Long> {

	RadioTourActivity activity = null;

	@Override
	protected Long doInBackground(RadioTourActivity... activity) {
		this.activity = activity[0];
		this.activity.importDriverandTeams();
		return null;
	}

	@Override
	protected void onPostExecute(Long result) {
		activity.showRaceFragment();
		activity.removeSplashScreen();
	}
}
