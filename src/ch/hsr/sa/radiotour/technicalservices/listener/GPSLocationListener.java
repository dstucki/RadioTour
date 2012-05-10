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
	private float distance;
	private boolean isRaceRunning = false;

	public String getSpeed() {
		return speed;
	}

	public float getDistance() {
		return Math.round(distance / 100f) / 10f;
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
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500l, 10,
				this);
	}

	public void stopLocationUpdates() {
		manager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		if (isRaceRunning)
			calculateDistance(newLocation);

		actualLocation = newLocation;
		getGPSData();
		setChanged();
		notifyObservers(this);
	}

	private void calculateDistance(Location newLocation) {
		if (actualLocation != null) {
			distance += actualLocation.distanceTo(newLocation);
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void getGPSData() {
		if (actualLocation != null) {
			if (actualLocation.hasAltitude()) {
				altitude = ((int) actualLocation.getAltitude()) + "";
			} else {
				altitude = "No GPS";
			}
			if (actualLocation.hasAccuracy() == true) {
				accuracy = round(actualLocation.getAccuracy()) + "";
			} else {
				accuracy = "No GPS";
			}
			if (actualLocation.hasSpeed()) {
				float kmh = round((float) (actualLocation.getSpeed() * 3.6));
				speed = kmh + "";
			} else {
				speed = "No GPS";
			}
		}
	}

	private static float round(float toRound) {
		return Math.round(toRound * 100f) / 100f;
	}

	public void startRace() {
		isRaceRunning = true;
	}

	public void stopRace() {
		isRaceRunning = false;
	}

}
