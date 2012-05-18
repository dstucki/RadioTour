package ch.hsr.sa.radiotour.technicalservices.connection;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
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
				RaceSituation current = queue
						.poll(10000, TimeUnit.MILLISECONDS);
				if (current != null) {
					try {
						makeRequest(current.toJSON());
					} catch (ClientProtocolException e) {
						Log.e(getClass().getSimpleName(), e.getMessage());
					} catch (IOException e) {
						Log.e(getClass().getSimpleName(), e.getMessage());
					}
				}

			} catch (InterruptedException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());

			} catch (JSONException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());

			}
		}

	}

	private void makeRequest(JSONObject json) throws ClientProtocolException,
			IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost("http://bentele.me/json/index.php");

		StringEntity se = new StringEntity(json.toString());
		httpost.setEntity(se);
		httpost.setHeader("Accept", "application/json");
		httpost.setHeader("Content-type", "application/json");

		ResponseHandler responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httpost, responseHandler);
	}

}
