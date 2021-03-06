package ch.hsr.sa.radiotour.domain;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.sa.radiotour.technicalservices.connection.JsonSendable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RiderStageConnection implements JsonSendable {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "rider")
	private Rider rider;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "etappe")
	private Stage stage;
	@DatabaseField()
	private int bonusTime;
	@DatabaseField
	private int bonusPoints;
	@DatabaseField
	private int officialRank;
	@DatabaseField(dataType = DataType.ENUM_STRING)
	private RiderState riderState;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date officialDeficit;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date officialTime;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date virtualDeficit;

	public RiderStageConnection(Stage stage, Rider rider, Date officialTime,
			Date officialDeficit) {
		this.stage = stage;
		this.rider = rider;
		this.officialTime = officialTime;
		this.officialDeficit = officialDeficit;
	}

	public RiderStageConnection() {

	}

	public RiderStageConnection(Stage stage, Rider rider) {
		this.stage = stage;
		this.rider = rider;
		this.riderState = RiderState.ACTIV;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public RiderState getRiderState() {
		if (riderState == null) {
			return RiderState.ACTIV;
		}
		return riderState;
	}

	public void setRiderState(RiderState riderState) {
		this.riderState = riderState;
	}

	public Date getOfficialDeficit() {
		if (officialDeficit == null) {
			return new Date(0, 0, 0, 0, 0, 0);
		}
		return officialDeficit;
	}

	public void setOfficialDeficit(Date officialDeficit) {
		this.officialDeficit = officialDeficit;
	}

	public Date getVirtualDeficit() {
		if (virtualDeficit == null) {
			return new Date(0, 0, 0, 0, 0, 0);
		}
		return virtualDeficit;
	}

	public void setVirtualDeficit(Date virtualDeficit) {
		this.virtualDeficit = virtualDeficit;
	}

	public int getBonusTime() {
		return bonusTime;
	}

	public void setBonusTime(int bonusTime) {
		this.bonusTime = bonusTime;
	}

	public Date getOfficialTime() {
		if (officialTime == null) {
			return new Date(0, 0, 0, 0, 0, 0);
		}
		return officialTime;
	}

	public void setOfficialTime(Date officialTime) {
		this.officialTime = officialTime;
	}

	public int getOfficialRank() {
		return officialRank;
	}

	public void setOfficialRank(int officialRank) {
		this.officialRank = officialRank;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("ridernr", rider.getStartNr());
		json.put("racekm", this.getRiderState());
		json.put("timestamp", new Date().getTime());
		return json;
	}

}
