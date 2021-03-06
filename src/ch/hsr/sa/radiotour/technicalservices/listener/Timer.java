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

	public void setBaseWhileRunning(long base) {
		timer.setBase(base);
	}

	public void setBaseWhileNotRunning(long time) {
		timer.setBase(SystemClock.elapsedRealtime() - time);
	}

	public void setTime() {
		timer.setBase(SystemClock.elapsedRealtime() - getDisplayedTime());
	}

	public double getRaceTimeInHour() {
		return (double) getDisplayedTime() / (double) 3600000;
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

	public long getDisplayedTime() {
		String array[] = timer.getText().toString().split(":");
		// 2 == min:sec
		// 3 == hour:min:sec
		if (array.length == 2) {
			stoppedTime = Integer.parseInt(array[0]) * 60 * 1000
					+ Integer.parseInt(array[1]) * 1000;
		} else if (array.length == 3) {
			stoppedTime = Integer.parseInt(array[0]) * 60 * 60 * 1000
					+ Integer.parseInt(array[1]) * 60 * 1000
					+ Integer.parseInt(array[2]) * 1000;
		}
		return stoppedTime;
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

	public Long getTimeInMillisec() {
		return SystemClock.elapsedRealtime() - timer.getBase();
	}

	public void setTimeInMillisec(Long ms) {
		stoppedTime = ms;
	}
}
