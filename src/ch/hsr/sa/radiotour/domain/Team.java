package ch.hsr.sa.radiotour.domain;

import java.util.ArrayList;

public class Team {
	private final ArrayList<Integer> driverNumbers;
	private String name;

	public Team(String name) {
		this.name = name;
		this.driverNumbers = new ArrayList<Integer>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Integer> getDriverNumbers() {
		return driverNumbers;
	}

}
