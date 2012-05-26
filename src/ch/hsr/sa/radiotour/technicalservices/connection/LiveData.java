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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LiveData extends Observable {
	private final String STATUS_URL = "http://www.tourlive.ch/tds12/json_public/status.php";
	private final int SPITZE = 0;
	private final int RADIOTOUR = 1;
	private final int FELD = 2;
	private ConnectionStatus connectionState;
	private ConnectionStatus spitze;
	private ConnectionStatus radiotour;
	private ConnectionStatus feld;
	private JSONArray jObject;
	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);

	public void updateperiodically() {
		final Runnable runner = new Runnable() {
			@Override
			public void run() {
				getNewLiveDataFromURL(STATUS_URL);
			};
		};
		@SuppressWarnings({ "rawtypes", "unused" })
		final ScheduledFuture schedulrate = scheduler.scheduleAtFixedRate(
				runner, 2, 10000, TimeUnit.MILLISECONDS);
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
				jObject = new JSONObject(distance.toString())
						.getJSONArray("sources");
				checkConnection();
			} else {
				connectionState = ConnectionStatus.RED;
				jObject = new JSONArray();
			}
		} catch (ClientProtocolException e) {
			connectionState = ConnectionStatus.RED;
		} catch (JSONException e) {
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

	public ConnectionStatus getSpitzeState() {
		return spitze;
	}

	public ConnectionStatus getFeldState() {
		return feld;
	}

	public ConnectionStatus getRadiotourState() {
		return radiotour;
	}

	public String getSpitzeFeldKm() {
		String value = " - ";
		try {
			int spi = Integer.valueOf(jObject.getJSONArray(SPITZE)
					.getJSONObject(0).getString("rennkilometer"));
			int fel = Integer.valueOf(jObject.getJSONArray(FELD)
					.getJSONObject(0).getString("rennkilometer"));
			value = String.valueOf(spi - fel) + " km";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getSpitzeFeldTime() {
		String value = " - ";
		// TODO: implement time calculation here
		return value;
	}

	public String getSpitzeRTKm() {
		String value = " - ";
		try {
			int spi = Integer.valueOf(jObject.getJSONArray(SPITZE)
					.getJSONObject(0).getString("rennkilometer"));
			int rt = Integer.valueOf(jObject.getJSONArray(RADIOTOUR)
					.getJSONObject(0).getString("rennkilometer"));
			value = String.valueOf(spi - rt) + " km";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getSpitzeRTTime() {
		String value = " - ";
		// TODO: implement time calculation here
		return value;
	}

	private void checkConnection() throws JSONException {
		if (jObject.getJSONArray(SPITZE).getJSONObject(0).getString("online") == "true") {
			spitze = ConnectionStatus.GREEN;
		} else {
			spitze = ConnectionStatus.RED;
		}
		if (jObject.getJSONArray(FELD).getJSONObject(0).getString("online") == "true") {
			feld = ConnectionStatus.GREEN;
		} else {
			feld = ConnectionStatus.RED;
		}
		if (jObject.getJSONArray(RADIOTOUR).getJSONObject(0)
				.getString("online") == "true") {
			radiotour = ConnectionStatus.GREEN;
		} else {
			radiotour = ConnectionStatus.RED;
		}
	}
}
