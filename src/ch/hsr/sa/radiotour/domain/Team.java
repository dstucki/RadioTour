package ch.hsr.sa.radiotour.domain;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Team")
public class Team {

	private List<Rider> riders;
	@DatabaseField(id = true)
	private String name;
	@DatabaseField
	private String shortName;

	public Team(String name) {
		this();
		this.name = name;
	}

	public Team() {
		riders = new ArrayList<Rider>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Rider> getDriverNumbers() {

		return riders;
	}

	public void addRider(Rider r) {
		riders.add(r);
	}

	public void setDriverNumbers(List<Rider> riders) {
		this.riders = riders;
	}

	public String getShortName() {
		if ("".equals(shortName) || null == shortName) {
			int endIndex = name.length() >= 3 ? 3 : name.length();
			return name.substring(0, endIndex);
		}
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
