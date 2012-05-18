package ch.hsr.sa.radiotour.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RaceSituation {
	private final List<Group> groups;
	private final long timestamp;
	private final float racekm;

	public RaceSituation(float km) {
		racekm = km;
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
}
