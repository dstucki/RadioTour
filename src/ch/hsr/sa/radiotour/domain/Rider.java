package ch.hsr.sa.radiotour.domain;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Rider {
	@DatabaseField(id = true)
	private int startNr;
	@DatabaseField
	private String name;
	@DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "team")
	private Team team;
	@DatabaseField
	private String country;
	@DatabaseField
	private Date birthday;
	@DatabaseField
	private String note;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean neo;

	public Rider() {

	}

	public Rider(int startnr, String name, Team team) {
		super();
		this.startNr = startnr;
		this.name = name;
		this.team = team;
	}

	public int getStartNr() {
		return startNr;
	}

	public void setStartNr(int startNr) {
		this.startNr = startNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean getNeo() {
		return neo;
	}

	public void setNeo(boolean neo) {
		this.neo = neo;
	}

	public Date getBirthday() {
		if (birthday == null) {
			return new Date();
		}
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return startNr + "   " + name;
	}

}
