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

	public Judgement(String name) {
		super();
		this.name = name;
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

}
