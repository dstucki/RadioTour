package ch.hsr.sa.radiotour.technicalservices.connection;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import ch.hsr.sa.radiotour.domain.RaceSituation;

public class JSONConnectionQueue {
	private final LinkedBlockingQueue<RaceSituation> queue = new LinkedBlockingQueue<RaceSituation>();
	private static JSONConnectionQueue instance = new JSONConnectionQueue();

	public static JSONConnectionQueue getInstance() {
		return instance;
	}

	public boolean addToQueue(RaceSituation situation) {
		return queue.add(situation);
	}

	public void start() {
		while (true) {
			try {
				RaceSituation current = queue.peek();
				if (current != null) {
					try {
						if (makeRequest(current.toJSON())) {
							queue.poll();

							Log.d(getClass().getSimpleName(), "Json sent");
						}

					} catch (IOException e) {
						// Connection failed, wait for next timeslot
						Thread.currentThread().sleep(10000);
						Log.e(getClass().getSimpleName(), e.getMessage());
					}
				} else {
					Thread.currentThread().sleep(10000);

				}
			} catch (JSONException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean makeRequest(JSONObject json)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost("http://bentele.me/json/index.php");

		StringEntity se = new StringEntity(json.toString());
		httpost.setEntity(se);
		httpost.setHeader("Accept", "application/json");
		httpost.setHeader("Content-type", "application/json");

		@SuppressWarnings("unchecked")
		HttpResponse response = httpclient.execute(httpost);
		Log.d(getClass().getSimpleName(), response.getStatusLine().toString()
				+ " " + response.getStatusLine().getStatusCode());

		return response.getStatusLine().getStatusCode() == 200;
	}

}
