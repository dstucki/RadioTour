package ch.hsr.sa.radiotour.technicalservices.connection;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

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
					Log.d(getClass().getSimpleName(), current.toJSON()
							.toString());
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
