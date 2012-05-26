package ch.hsr.sa.radiotour.domain;

import java.util.Date;
import java.util.Observable;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Group")
public class Group extends Observable implements Comparable<Group> {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private TreeSet<Integer> driverNumbers;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean isField = false;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean isLeader = false;
	@DatabaseField(dataType = DataType.DATE)
	private Date handicapTime;
	@DatabaseField(dataType = DataType.DATE)
	private Date lastHandiCap;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "racesituation")
	private RaceSituation situation;
	@DatabaseField
	private int orderNumber;

	public Group() {
		this.driverNumbers = new TreeSet<Integer>();
	}

	public TreeSet<Integer> getDriverNumbers() {
		return driverNumbers;
	}

	public void setDriverNumbers(TreeSet<Integer> driverNumbers) {
		this.driverNumbers = driverNumbers;
	}

	public void removeDriverNumber(Integer driverNumber) {
		driverNumbers.remove(driverNumber);
	}

	public boolean isField() {
		return isField;
	}

	public void setField(boolean flag) {
		isField = flag;
	}

	public Date getHandicapTime() {
		return handicapTime;
	}

	public void setHandicapTime(Date handicapTime) {
		this.handicapTime = handicapTime;
	}

	public void updateHandicapTime(Date handicapTime) {
		Date last;
		if (this.handicapTime == null) {
			last = new Date(0, 0, 0, 0, 0, 0);
		} else {
			last = new Date(this.handicapTime.getTime());
		}
		setHandicapTime(handicapTime);
		setLastHandiCap(last);
		setChanged();
		notifyObservers();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	private long getHandicapInMilliseconds() {
		return handicapTime == null ? 0L : handicapTime.getTime();
	}

	@Override
	public int compareTo(Group another) {
		return orderNumber - another.orderNumber;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("groupnr", orderNumber);
			json.put("isLeader", isLeader);
			json.put("isField", isField);
			if (!isField) {
				json.put("drivernumber", new JSONArray(driverNumbers));
			}
			json.put("handicaptime", getHandicapInMilliseconds());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public RaceSituation getSituation() {
		return situation;
	}

	public void setSituation(RaceSituation situation) {
		this.situation = situation;
	}

	public boolean isLeader() {
		return isLeader;
	}

	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}

	public Date getLastHandiCap() {
		return lastHandiCap;
	}

	public void setLastHandiCap(Date lastHandiCap) {
		this.lastHandiCap = lastHandiCap;
	}

}
