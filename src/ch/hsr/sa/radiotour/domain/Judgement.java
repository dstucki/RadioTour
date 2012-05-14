package ch.hsr.sa.radiotour.domain;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Judgement {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField()
	private String name;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private int[] winningRiders;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "specialranking")
	private SpecialRanking ranking;
	@DatabaseField()
	private double distance;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "etappe")
	private Stage stage;

	public Judgement(String name, double distance, Stage stage) {
		super();
		this.name = name;
		this.setDistance(distance);
		this.setStage(stage);
	}

	public Judgement() {
	};

	public int[] getWinningRiders() {
		return winningRiders;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setWinningRiders(int[] winningRiders) {
		this.winningRiders = winningRiders;
	}

	public void addWinner(int rank, int riderNr) {
		winningRiders[rank - 1] = riderNr;
	}

	@Override
	public String toString() {
		return name;
	}

	public SpecialRanking getRanking() {
		return ranking;
	}

	public void setRanking(SpecialRanking ranking) {
		this.ranking = ranking;
		winningRiders = new int[ranking.getNrOfWinningDrivers()];
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

}
