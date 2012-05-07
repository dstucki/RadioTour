package ch.hsr.sa.radiotour.domain;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class PointOfRace {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int altitude;
	@DatabaseField
	private double distance;
	@DatabaseField
	private String name;
	@DatabaseField(dataType = DataType.DATE)
	private Date estimatedDate;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Stage stage;

	public PointOfRace(int altitude, double distance, String name,
			Date estimatedDate) {
		this.altitude = altitude;
		this.distance = distance;
		this.name = name;
		this.estimatedDate = estimatedDate;
	}

	public PointOfRace() {

	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getEstimatedDate() {
		return estimatedDate;
	}

	public void setEstimatedDate(Date estimatedDate) {
		this.estimatedDate = estimatedDate;
	}
}
