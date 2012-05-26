package ch.hsr.sa.radiotour.technicalservices.connection;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonSendable {
	public JSONObject toJSON() throws JSONException;
}
