package ch.hsr.sa.radiotour.fragments;

import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.technicalservices.connection.ConnectionStatus;
import ch.hsr.sa.radiotour.technicalservices.connection.LiveData;
import ch.hsr.sa.radiotour.technicalservices.listener.GPSLocationListener;
import ch.hsr.sa.radiotour.technicalservices.listener.Timer;

public class HeaderFragment extends Fragment implements Observer {
	// Flo's Stuff
	private Timer stopWatchTimer;
	private Timer racetimeTimer;
	private Button startstoprace;
	private Button startstopwatch;
	private Button reset;
	private GPSLocationListener mGPS;
	private LiveData updatedLiveData;
	private TextView tabRen;

	// end Flo's Stuff

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
		View view = inflater
				.inflate(R.layout.header_fragment, container, false);

		// Flo's Stuff

		tabRen = (TextView) view.findViewById(R.id.tab_ren);
		tabRen.setOnClickListener(tabclicklistener);

		stopWatchTimer = new Timer(
				(Chronometer) view.findViewById(R.id.chrono_stopwatch));
		racetimeTimer = new Timer(
				(Chronometer) view.findViewById(R.id.chrono_racetime));

		startstopwatch = (Button) view
				.findViewById(R.id.bt_stopwatch_start_stop);
		startstopwatch.setOnClickListener(stopWatchListener);
		reset = (Button) view.findViewById(R.id.bt_stopwatch_reset);
		reset.setOnClickListener(stopWatchResetListener);

		startstoprace = (Button) view.findViewById(R.id.bt_racetime_start_stop);
		startstoprace.setOnClickListener(racetimeListener);

		mGPS = new GPSLocationListener(getActivity().getApplicationContext());
		mGPS.addObserver(this);

		updatedLiveData = new LiveData();
		updatedLiveData.updateperiodically();
		updatedLiveData.addObserver(this);
		// end Flo's Stuff

		return view;
	}

	// Flo's Stuff
	View.OnClickListener tabclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(getClass().getSimpleName(), "Clicked");
		}
	};

	View.OnClickListener racetimeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			racetimeTimer.toggle();
			if (racetimeTimer.isRunning()) {
				startstoprace.setText(R.string.stop);
			} else {
				startstoprace.setText(R.string.start);
			}
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

	View.OnClickListener stopWatchResetListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			stopWatchTimer.reset();
			startstopwatch.setText(R.string.start);
		}
	};

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof GPSLocationListener) {
			GPSLocationListener temp = (GPSLocationListener) data;
			TextView speedo = (TextView) getView().findViewById(
					R.id.speed_value);
			speedo.setText(temp.getSpeed());
			TextView altitude = (TextView) getView().findViewById(
					R.id.altitude_value);
			altitude.setText(temp.getAltitude());

		} else if (data instanceof LiveData) {
			final LiveData livedata = (LiveData) data;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ImageView connectionImage = (ImageView) getView()
							.findViewById(R.id.img_connection);
					ConnectionStatus connectionState = livedata
							.getConnectionState();
					if (connectionState == ConnectionStatus.RED) {
						connectionImage.setImageResource(R.drawable.red);
					} else if (connectionState == ConnectionStatus.GREEN) {
						connectionImage.setImageResource(R.drawable.green);
						TextView spitzefeld = (TextView) getView()
								.findViewById(R.id.spitzefeld_value);
						spitzefeld.setText(livedata.getSpitzeFeld());
						TextView spitzert = (TextView) getView().findViewById(
								R.id.spitzert_value);
						spitzert.setText(livedata.getSpitzeRT());
					}
				}
			});
		}
	}

}
