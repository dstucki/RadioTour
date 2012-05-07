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

}
