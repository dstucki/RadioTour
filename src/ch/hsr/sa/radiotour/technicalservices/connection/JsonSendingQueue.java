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

public class JsonSendingQueue {
	private final LinkedBlockingQueue<JsonSendable> queue = new LinkedBlockingQueue<JsonSendable>();
	private final String JSON_URL = "http://bentele.me/json/index.php";
	private static JsonSendingQueue instance = new JsonSendingQueue();

	private JsonSendingQueue() {
	}

	public static JsonSendingQueue getInstance() {
		return instance;
	}

	public boolean addToQueue(JsonSendable situation) {
		return queue.add(situation);
	}

	public void start() {
		while (true) {
			try {
				JsonSendable current = queue.peek();
				if (current != null) {
					try {
						if (makeRequest(current.toJSON())) {
							queue.poll();
						}
					} catch (IOException e) {
						Thread.sleep(10000);
					}
				} else {
					Thread.sleep(10000);
				}
			} catch (JSONException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
			} catch (InterruptedException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
			}
		}

	}

	private boolean makeRequest(JSONObject json)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(JSON_URL);

		StringEntity se = new StringEntity(json.toString());
		httpost.setEntity(se);
		httpost.setHeader("Accept", "application/json");
		httpost.setHeader("Content-type", "application/json");
		HttpResponse response = httpclient.execute(httpost);
		return response.getStatusLine().getStatusCode() == 200;
	}

}
