package ch.hsr.sa.radiotour.technicalservices.listener;

import java.util.Observable;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSLocationListener extends Observable implements LocationListener {
	private final LocationManager manager;
	private String speed = "No GPS";
	private String altitude = "No GPS";
	private String accuracy = "No GPS";
	private Location actualLocation;

	public String getSpeed() {
		return speed;
	}

	public String getAltitude() {
		return altitude;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public GPSLocationListener(Context context) {
		manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (manager != null) {
			actualLocation = manager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		startLocationUpdates();
	}

	public void startLocationUpdates() {
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500l, 1,
				this);
	}

	public void stopLocationUpdates() {
		manager.removeUpdates(this);
	}

	public void onLocationChanged(Location newLocation) {
		actualLocation = newLocation;
		setChanged();
		notifyObservers(this);
	}

	public void onProviderEnabled(String provider) {
	}

	public void onProviderDisabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void getGPSData() {
		if (actualLocation != null) {
			if (actualLocation.hasAltitude()) {
				altitude = actualLocation.getAltitude() + " MŸM";
			} else {
				altitude = "No GPS Data";
			}
			if (actualLocation.hasAccuracy() == true) {
				accuracy = round(actualLocation.getAccuracy()) + " ";
			} else {
				accuracy = "No GPS Data";
			}
			if (actualLocation.hasSpeed()) {
				float kmh = round((float) (actualLocation.getSpeed() * 3.6));
				speed = kmh + "km/h";
			} else {
				speed = "No GPS Data";
			}
		}
	}

	private static float round(float toRound) {
		return Math.round(toRound * 100f) / 100f;
	}

}
