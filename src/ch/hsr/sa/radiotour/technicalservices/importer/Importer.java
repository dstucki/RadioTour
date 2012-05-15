package ch.hsr.sa.radiotour.technicalservices.importer;

import android.os.AsyncTask;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;

public class Importer extends AsyncTask<RadioTourActivity, Integer, Long> {

	RadioTourActivity a = null;

	@Override
	protected Long doInBackground(RadioTourActivity... activity) {
		a = activity[0];
		a.importDriverandTeams();
		return null;
	}

	@Override
	protected void onPostExecute(Long result) {
		a.showRaceFragment();
	}
}
