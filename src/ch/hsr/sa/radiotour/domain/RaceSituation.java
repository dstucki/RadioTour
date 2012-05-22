package ch.hsr.sa.radiotour.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RaceSituation {
	private List<Group> groups;
	@DatabaseField(id = true)
	private long timestamp;
	@DatabaseField
	private float racekm;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "etappe")
	private Stage stage;

	public RaceSituation(float km, Stage stage) {
		this();
		racekm = km;
		this.setStage(stage);
	}

	public RaceSituation() {
		groups = new LinkedList<Group>();
		timestamp = new Date().getTime();

	}

	public JSONObject toJSON() throws JSONException {
		JSONArray ar = new JSONArray();
		for (Group group : groups) {
			ar.put(group.toJson());
		}
		JSONObject json = new JSONObject();
		json.put("timestamp", timestamp);
		json.put("racekm", racekm);
		json.put("group", ar);
		return json;
	}

	public void add(Group gr) {
		groups.add(gr);
	}

	public void addAll(List<Group> group) {
		groups.addAll(group);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public float getRacekm() {
		return racekm;
	}

	public void setRacekm(float racekm) {
		this.racekm = racekm;
	}
}
