package ch.hsr.sa.radiotour.technicalservices.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class LiveData extends Observable {
	private final String URL = "http://bentele.me/radiotour/";
	private ConnectionStatus connectionState;
	private JSONObject jObject;
	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);

	public void updateperiodically() {
		final Runnable beeper = new Runnable() {
			@Override
			public void run() {
				getNewLiveDataFromURL(URL);
			};
		};
		@SuppressWarnings("rawtypes")
		final ScheduledFuture beepHandler = scheduler.scheduleAtFixedRate(
				beeper, 2, 10, TimeUnit.SECONDS);
		beepHandler.toString();
	}

	private void getNewLiveDataFromURL(String url) {
		StringBuilder distance = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet getdata = new HttpGet(url);
		try {
			HttpResponse response = client.execute(getdata);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			// http 200 is "ok"
			if (statusCode == 200) {
				connectionState = ConnectionStatus.GREEN;
				HttpEntity entity = response.getEntity();
				InputStream data = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(data));
				String line;
				while ((line = reader.readLine()) != null) {
					distance.append(line);
				}
				try {
					jObject = new JSONObject(distance.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				connectionState = ConnectionStatus.RED;
				jObject = new JSONObject();
			}
		} catch (ClientProtocolException e) {
			connectionState = ConnectionStatus.RED;
		} catch (IOException e) {
			connectionState = ConnectionStatus.RED;

		}
		setChanged();
		notifyObservers(this);
	}

	public ConnectionStatus getConnectionState() {
		return connectionState;
	}

	public String getSpitzeFeldKm() {
		String value = "No connection";
		try {
			value = jObject.getString("spitzefeld_km");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getSpitzeFeldTime() {
		String value = "No connection";
		try {
			value = jObject.getString("spitzefeld_time");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getSpitzeRTKm() {
		String value = " - ";
		try {
			value = jObject.getString("spitzert_km");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getSpitzeRTTime() {
		String value = " - ";
		try {
			value = jObject.getString("spitzert_time");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
}
