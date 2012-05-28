package ch.hsr.sa.radiotour.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class MaillotStageConnection {
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "maillot")
	private Maillot maillot;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "etappe")
	private Stage stage;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "rider")
	private Rider rider;

	public MaillotStageConnection() {
	}

	public MaillotStageConnection(Maillot maillot, Stage stage, Rider rider) {
		this.maillot = maillot;
		this.stage = stage;
		this.rider = rider;
	}

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	public Maillot getMaillot() {
		return maillot;
	}

	public void setMaillot(Maillot ma) {
		maillot = ma;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
