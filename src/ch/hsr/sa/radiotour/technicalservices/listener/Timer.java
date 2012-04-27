package ch.hsr.sa.radiotour.technicalservices.listener;

import android.os.SystemClock;
import android.widget.Chronometer;

public class Timer {
	private final Chronometer timer;
	private long stoppedTime;
	private boolean timerRunning;

	public Timer(Chronometer chrono) {
		stoppedTime = 0;
		timerRunning = false;
		timer = chrono;
	}

	public long getTime() {
		return timer.getBase();
	}

	public void reset() {
		timer.stop();
		timer.setBase(SystemClock.elapsedRealtime());
		stoppedTime = 0;
		timerRunning = false;
	}

	public void stop() {
		stoppedTime = SystemClock.elapsedRealtime() - timer.getBase();
		timer.stop();
		timerRunning = false;
	}

	public void start() {
		timer.setBase(SystemClock.elapsedRealtime() - stoppedTime);
		timer.start();
		timerRunning = true;
	}

	public void toggle() {
		if (timerRunning) {
			this.stop();
		} else {
			this.start();
		}
	}

	public boolean isRunning() {
		return timerRunning;
	}
}
