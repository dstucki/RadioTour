package ch.hsr.sa.radiotour.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Stage {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String start;
	@DatabaseField
	private String destination;
	@DatabaseField
	private double wholeDistance;

	public Stage(String start, String destination) {
		this.start = start;
		this.destination = destination;
	}

	public Stage(String start, String destination, double distance) {
		this(start, destination);
		this.wholeDistance = distance;
	}

	public Stage() {

	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public double getWholeDistance() {
		return wholeDistance;
	}

	public void setWholeDistance(double wholeDistance) {
		this.wholeDistance = wholeDistance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id + ". " + start + " - " + destination + " " + wholeDistance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Stage)) {
			return false;
		}
		return (((Stage) o).getId() == this.getId());
	}

	@Override
	public int hashCode() {
		return getId();
	}

}
