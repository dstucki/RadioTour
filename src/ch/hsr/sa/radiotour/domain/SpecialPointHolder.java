package ch.hsr.sa.radiotour.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class SpecialPointHolder {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "rider")
	private Rider rider;
	@DatabaseField()
	private int timeBoni = 0;
	@DatabaseField()
	private int pointBoni = 0;
	@DatabaseField()
	private int rank;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "judgement")
	private Judgement judgement;

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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Judgement getJudgement() {
		return judgement;
	}

	public void setJudgement(Judgement judgement) {
		this.judgement = judgement;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
