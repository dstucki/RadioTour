package ch.hsr.sa.radiotour.domain;

public class SpecialPointHolder {
	private BicycleRider rider;
	private int timeBoni = 0;
	private int pointBoni = 0;

	public BicycleRider getRider() {
		return rider;
	}

	public void setRider(BicycleRider rider) {
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
