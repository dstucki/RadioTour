package ch.hsr.sa.radiotour.domain;

public class SpecialPointHolder {
	private Rider rider;
	private int timeBoni = 0;
	private int pointBoni = 0;

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
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

}
