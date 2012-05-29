package ch.hsr.sa.radiotour.domain;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Judgement {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField()
	private String name;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "specialranking")
	private SpecialRanking ranking;
	@DatabaseField()
	private double distance;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "etappe")
	private Stage stage;
	@DatabaseField()
	private int nrOfWinningRiders;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean timeBoni;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean pointBoni;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<Integer> pointBonis;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<Integer> timeBonis;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<Integer> winningRiders;

	public Judgement(String name, double distance, Stage stage) {
		this();
		this.name = name;
		this.distance = distance;
		this.stage = stage;
	}

	public Judgement() {
		pointBonis = new ArrayList<Integer>();
		timeBonis = new ArrayList<Integer>();
		winningRiders = new ArrayList<Integer>();
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SpecialRanking getRanking() {
		return ranking;
	}

	public void setRanking(SpecialRanking ranking) {
		this.ranking = ranking;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public int getNrOfWinningRiders() {
		return nrOfWinningRiders;
	}

	public void setNrOfWinningDrivers(int nrOfWinningRiders) {
		this.nrOfWinningRiders = nrOfWinningRiders;
		int oldSize = pointBonis.size();
		if (oldSize == nrOfWinningRiders) {
			return;
		}
		if (oldSize < nrOfWinningRiders) {
			while (pointBonis.size() < nrOfWinningRiders) {
				pointBonis.add(0);
				timeBonis.add(0);
				winningRiders.add(0);
			}
			return;
		} else {
			while (pointBonis.size() > nrOfWinningRiders) {
				pointBonis.remove(pointBonis.size() - 1);
				timeBonis.remove(timeBonis.size() - 1);
				winningRiders.remove(winningRiders.size() - 1);
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

	public void setWinningRiders(ArrayList<Integer> tempArray) {
		if (tempArray == null) {
			return;
		}
		this.winningRiders = tempArray;
	}

	public List<Integer> getWinningRiders() {
		return winningRiders;
	}

}
