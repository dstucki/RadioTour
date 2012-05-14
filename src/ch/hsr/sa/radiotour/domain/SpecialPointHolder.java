package ch.hsr.sa.radiotour.domain;

public class SpecialPointHolder {
	public int getRider() {
		return rider;
	}

	public void setRider(int rider) {
		this.rider = rider;
	}

	public int getTimeBoni() {
		return timeBoni;
	}

	public void addTimeBoni(int timeBoni) {
		this.timeBoni += timeBoni;
	}

	public int getPointBoni() {
		return pointBoni;
	}

	public void addPointBoni(int pointBoni) {
		this.pointBoni += pointBoni;
	}

	private int rider;
	private int timeBoni = 0;
	private int pointBoni = 0;
}
