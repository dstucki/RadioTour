package ch.hsr.sa.radiotour.domain;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class SpecialRanking {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField()
	private String name;
	@DatabaseField()
	private int nrOfWinningDrivers;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean timeBoni;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean pointBoni;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<Integer> pointBonis;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<Integer> timeBonis;

	public SpecialRanking(String name, int nrOfwinningDrivers,
			boolean timeBoni, boolean pointBoni, List<Integer> timeBonis,
			List<Integer> pointBonis) {
		this.name = name;
		this.nrOfWinningDrivers = nrOfwinningDrivers;
		this.timeBoni = timeBoni;
		this.pointBoni = pointBoni;
		this.pointBonis = new ArrayList<Integer>(pointBonis);
		this.timeBonis = new ArrayList<Integer>(timeBonis);
	}

	public SpecialRanking() {
		this.pointBonis = new ArrayList<Integer>(nrOfWinningDrivers);
		this.timeBonis = new ArrayList<Integer>(nrOfWinningDrivers);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNrOfWinningDrivers() {
		return nrOfWinningDrivers;
	}

	public void setNrOfWinningDrivers(int nrOfWinningDrivers) {
		this.nrOfWinningDrivers = nrOfWinningDrivers;
		if (isTimeBoni()) {
			while (timeBonis.size() < nrOfWinningDrivers) {
				timeBonis.add(0);
			}
		}
		if (isPointBoni()) {
			while (pointBonis.size() < nrOfWinningDrivers) {
				pointBonis.add(0);
			}
		}
	}

	public boolean isTimeBoni() {
		return timeBoni;
	}

	public void setTimeBoni(boolean timeBoni) {
		this.timeBoni = timeBoni;
	}

	public boolean isPointBoni() {
		return pointBoni;
	}

	public void setPointBoni(boolean pointBoni) {
		this.pointBoni = pointBoni;
	}

	public ArrayList<Integer> getPointBonis() {
		return pointBonis;
	}

	public void setPointBonis(ArrayList<Integer> pointBonis) {
		this.pointBonis = pointBonis;
	}

	public ArrayList<Integer> getTimeBonis() {
		return timeBonis;
	}

	public void setTimeBonis(ArrayList<Integer> timeBonis) {
		this.timeBonis = timeBonis;
	}

	@Override
	public String toString() {
		return name;
	}

}
