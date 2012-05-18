package ch.hsr.sa.radiotour.technicalservices.listener;

import java.util.Observable;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import ch.hsr.sa.radiotour.technicalservices.connection.ConnectionStatus;

public class GPSLocationListener extends Observable implements LocationListener {
	private LocationManager manager = null;
	private ConnectionStatus connectionState;
	private final String speed = "-";
	private String altitude = "-";
	private String accuracy = "-";
	private Location actualLocation;
	private float distanceInMeters;
	private boolean isRaceRunning = false;

	public String getSpeed() {
		return speed;
	}

	public float getDistanceInKm() {
		return Math.round(distanceInMeters / 100f) / 10f;
	}

	public void setDistance(float distanceInKM) {
		distanceInMeters = distanceInKM * 1000;
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
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500L, 10,
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
		updateLocation();
	}

	public void updateLocation() {
		setChanged();
		notifyObservers(this);
	}

	private void calculateDistance(Location newLocation) {
		if (actualLocation != null) {
			distanceInMeters += actualLocation.distanceTo(newLocation);
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	public void getGPSData() {
		if (actualLocation != null) {
			if (actualLocation.hasAltitude()) {
				altitude = ((int) actualLocation.getAltitude()) + "";
			} else {
				altitude = "No GPS";
			}
			if (actualLocation.hasAccuracy()) {
				accuracy = round(actualLocation.getAccuracy()) + "";
			} else {
				accuracy = "No GPS";
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

	public ConnectionStatus getConnectionState() {
		return connectionState;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle arg2) {
		Log.d(getClass().getSimpleName(), arg2.describeContents() + "");
		Log.d(getClass().getSimpleName(), arg2.toString());

		if (status == LocationProvider.AVAILABLE) {
			connectionState = ConnectionStatus.GREEN;
		} else {
			connectionState = ConnectionStatus.RED;
		}
	}
}
