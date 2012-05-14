package ch.hsr.sa.radiotour.technicalservices.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import ch.hsr.sa.radiotour.technicalservices.listener.GPSLocationListener;
import ch.hsr.sa.radiotour.technicalservices.listener.Timer;

public class SharedPreferencesHelper {
	private static SharedPreferences settings;
	private static SharedPreferencesHelper helper;
	private final String PREFS_NAME = "RadioTourValues";

	private SharedPreferencesHelper(Context con) {
		settings = con.getSharedPreferences(PREFS_NAME, 0);
	}

	public static void initializePreferences(Context con) {

		if (helper == null) {
			helper = new SharedPreferencesHelper(con);
		}
	}

	public static SharedPreferencesHelper preferences() {
		return helper;
	}

	public void checkPersitentKm(GPSLocationListener gps) {
		if (gps.getDistanceInKm() != settings.getFloat("km", 0f)) {
			gps.setDistance(settings.getFloat("km", 0f));
		}
	}

	public void setPersistentKm(float km) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("km", km);
		editor.commit();
	}

	public void checkPersitentTime(Timer time) {
		if (time.getTimeInMillisec() != settings.getLong("racetime", 0)) {
			time.setTimeInMillisec(settings.getLong("racetime", 0));
		}
	}

	public void setPersistentTime(Long ms) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("racetime", ms);
		editor.commit();
	}

}
