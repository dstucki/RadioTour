package ch.hsr.sa.radiotour.fragments;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.fragments.interfaces.TimePickerIF;
import ch.hsr.sa.radiotour.technicalservices.connection.ConnectionStatus;
import ch.hsr.sa.radiotour.technicalservices.connection.LiveData;
import ch.hsr.sa.radiotour.technicalservices.listener.GPSLocationListener;
import ch.hsr.sa.radiotour.technicalservices.listener.Timer;
import ch.hsr.sa.radiotour.technicalservices.sharedpreferences.SharedPreferencesHelper;
import ch.hsr.sa.radiotour.utils.StringUtils;

public class HeaderFragment extends Fragment implements Observer, TimePickerIF {
	private Timer stopWatchTimer;
	private static Timer racetimeTimer;
	private Button startstoprace;
	private Button startstopwatch;
	private static GPSLocationListener mGPS;
	private LiveData updatedLiveData;
	private TextView tabRen;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.header_fragment, container, false);

		SharedPreferencesHelper.initializePreferences(getActivity()
				.getApplicationContext());

		tabRen = (TextView) view.findViewById(R.id.tab_adm);
		tabRen.setOnClickListener(tabclicklistener);
		((ViewGroup) view).removeView(tabRen); // FIXME Berner Rudnfahrt
		tabRen = (TextView) view.findViewById(R.id.tab_spez);
		tabRen.setOnClickListener(tabclicklistener);
		((ViewGroup) view).removeView(tabRen); // FIXME Berner Rudnfahrt

		tabRen = (TextView) view.findViewById(R.id.tab_vir);
		tabRen.setOnClickListener(tabclicklistener);
		tabRen = (TextView) view.findViewById(R.id.tab_ren);
		tabRen.setOnClickListener(tabclicklistener);

		stopWatchTimer = new Timer(
				(Chronometer) view.findViewById(R.id.chrono_stopwatch));
		racetimeTimer = new Timer(
				(Chronometer) view.findViewById(R.id.chrono_racetime));

		((TextView) view.findViewById(R.id.lb_stage))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						((RadioTourActivity) getActivity())
								.showMarchTableDialog();
					}
				});

		startstopwatch = (Button) view
				.findViewById(R.id.bt_stopwatch_start_stop);
		startstopwatch.setOnClickListener(stopWatchListener);
		startstopwatch.setOnLongClickListener(stopWatchResetListener);

		startstoprace = (Button) view.findViewById(R.id.bt_racetime_start_stop);
		startstoprace.setOnClickListener(racetimeListener);

		((Chronometer) view.findViewById(R.id.chrono_racetime))
				.setOnClickListener(editRacetimeListener);

		((TextView) view.findViewById(R.id.distance_value))
				.setOnClickListener(editRacekmListener);
		((TextView) view.findViewById(R.id.etappe_value))
				.setText(((RadioTour) getActivity().getApplication())
						.getActualSelectedStage().getId() + "");

		mGPS = new GPSLocationListener(getActivity().getApplicationContext());
		mGPS.addObserver(this);

		SharedPreferencesHelper.preferences().checkPersitentKm(mGPS);
		SharedPreferencesHelper.preferences().checkPersitentTime(racetimeTimer);
		raceTimeButtonLabelChecker();

		updatedLiveData = new LiveData();
		updatedLiveData.updateperiodically();
		updatedLiveData.addObserver(this);

		return view;
	}

	View.OnClickListener tabclicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			resetTabBar(view);
			switch (v.getId()) {
			case R.id.tab_ren:
				((RadioTourActivity) getActivity()).ontestButtonClick(v);
				((Button) view.findViewById(R.id.tab_ren))
						.setBackgroundColor(0xFF434343);
				return;
			case R.id.tab_adm:
				((RadioTourActivity) getActivity()).onAdminButtonClick(v);
				((Button) view.findViewById(R.id.tab_adm))
						.setBackgroundColor(0xFF434343);
				return;
			case R.id.tab_spez:
				((RadioTourActivity) getActivity()).onSpecialButtonClick(v);
				((Button) view.findViewById(R.id.tab_spez))
						.setBackgroundColor(0xFF434343);
				return;
			case R.id.tab_vir:
				((RadioTourActivity) getActivity()).ontestButtonClick1(v);
				((Button) view.findViewById(R.id.tab_vir))
						.setBackgroundColor(0xFF434343);
				return;
			default:
				Log.e(getClass().getSimpleName(), "I'm default case");
			}
		}
	};

	View.OnClickListener racetimeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			racetimeTimer.toggle();
			raceTimeButtonLabelChecker();
		}
	};

	View.OnClickListener stopWatchListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			stopWatchTimer.toggle();
			if (stopWatchTimer.isRunning()) {
				startstopwatch.setText(R.string.stop);
			} else {
				startstopwatch.setText(R.string.start);
			}
		}
	};

	View.OnLongClickListener stopWatchResetListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			stopWatchTimer.reset();
			startstopwatch.setText(R.string.start);
			return true;
		}
	};

	View.OnClickListener editRacetimeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			((RadioTourActivity) getActivity()).showTimeDialog(
					HeaderFragment.this, true);

		}
	};

	View.OnClickListener editRacekmListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			((RadioTourActivity) getActivity()).showKmDialog(mGPS);

		}
	};

	private void resetTabBar(View v) {
		((Button) v.findViewById(R.id.tab_adm)).setBackgroundColor(0);
		((Button) v.findViewById(R.id.tab_spez)).setBackgroundColor(0);
		((Button) v.findViewById(R.id.tab_vir)).setBackgroundColor(0);
		((Button) v.findViewById(R.id.tab_ren)).setBackgroundColor(0);
	}

	@Override
	public void update(Observable observable, Object data) {

		if (data instanceof GPSLocationListener) {
			mGPS = (GPSLocationListener) data;

			updateGPSValues();
		} else if (data instanceof LiveData) {

			final LiveData livedata = (LiveData) data;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateGPSValues();

					TextView connectionImage = (TextView) getView()
							.findViewById(R.id.img_connection);
					ConnectionStatus connectionState = livedata
							.getConnectionState();

					if (updateConnectionIMGs(connectionState, connectionImage)) {

						TextView spitzefeld = (TextView) getView()
								.findViewById(R.id.spitzefeld_value_km);
						spitzefeld.setText(livedata.getSpitzeFeldKm());
						TextView spitzert = (TextView) getView().findViewById(
								R.id.spitzert_value_km);
						spitzert.setText(livedata.getSpitzeRTKm());

						TextView spitzefeldtime = (TextView) getView()
								.findViewById(R.id.spitzefeld_value_time);
						spitzefeldtime.setText(livedata.getSpitzeFeldTime());
						TextView spitzerttime = (TextView) getView()
								.findViewById(R.id.spitzert_value_time);
						spitzerttime.setText(livedata.getSpitzeRTTime());
					}
				}
			});
		}
	}

	private boolean updateConnectionIMGs(ConnectionStatus connectionState,
			TextView connectionImage) {
		if (connectionState == ConnectionStatus.GREEN) {
			Drawable img = getResources().getDrawable(
					R.drawable.trafficlight_green);
			img.setBounds(0, 0, 16, 16);
			connectionImage.setCompoundDrawables(null, null, img, null);
			return true;
		} else {
			Drawable img = getResources().getDrawable(
					R.drawable.trafficlight_red);
			img.setBounds(0, 0, 16, 16);
			connectionImage.setCompoundDrawables(null, null, img, null);
			return false;
		}
	}

	private void updateGPSValues() {
		TextView speedo = (TextView) getView().findViewById(R.id.speed_value);
		speedo.setText(calculateSpeed());
		TextView altitude = (TextView) getView().findViewById(
				R.id.altitude_value);
		altitude.setText(mGPS.getAltitude() + " müM");
		setDistance(mGPS.getDistanceInKm());
	}

	private String calculateSpeed() {
		Float dist = mGPS.getDistanceInKm();
		double time = (racetimeTimer.getRaceTimeInHour());
		if (dist != 0 && time != 0) {
			return (Math.round((dist / time) * 10f)) / 10f + " km/h";
		}
		return "0 km/h";
	}

	private void setDistance(float dist) {
		TextView distance = (TextView) view.findViewById(R.id.distance_value);
		distance.setText(dist + " km");
	}

	@Override
	public Date getTime() {
		return new Date(racetimeTimer.getDisplayedTime());
	}

	@Override
	public void setTime(Date date) {
		((Chronometer) view.findViewById(R.id.chrono_racetime))
				.setText(StringUtils.getTimeAsString(date));
		racetimeTimer.setTime();
	}

	public static void saveDistance() {
		SharedPreferencesHelper.preferences().setPersistentKm(
				mGPS.getDistanceInKm());
	}

	public static void saveRaceTime() {
		if (racetimeTimer.isRunning()) {
			SharedPreferencesHelper.preferences().setPersistentTime(
					racetimeTimer.getTime(), racetimeTimer.isRunning());
		} else {
			SharedPreferencesHelper.preferences()
					.setPersistentTime(racetimeTimer.getDisplayedTime(),
							racetimeTimer.isRunning());
		}
	}

	private void raceTimeButtonLabelChecker() {
		if (racetimeTimer.isRunning()) {
			startstoprace.setText(R.string.stop);
			mGPS.startRace();
		} else {
			startstoprace.setText(R.string.start);
			mGPS.stopRace();
		}
	}

	public void updateStage(Stage stage) {
		Log.i(getClass().getSimpleName(), "updaeStage " + stage.toString());
		((TextView) view.findViewById(R.id.etappe_value)).setText(stage.getId()
				+ "");
		updateKmToGo(stage);
	}

	private void updateKmToGo(Stage stage) {
		((TextView) view.findViewById(R.id.distancetogo)).setText("-"
				+ (String.valueOf(Math.round((stage.getWholeDistance() - mGPS
						.getDistanceInKm()) * 100f) / 100f)) + " km");
	}
}
