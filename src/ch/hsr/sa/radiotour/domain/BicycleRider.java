package ch.hsr.sa.radiotour.domain;

import java.util.Date;

public class BicycleRider {
	private int startNr;
	private String name;
	private String teamName;
	private String teamShort;
	private String country;
	private Date birthday;

	public BicycleRider() {

	}

	public BicycleRider(int startnr,String name, String teamName, String teamShort,
			String country, Date birthday) {
		super();
		this.startNr=startnr;
		this.name = name;
		this.teamName = teamName;
		this.teamShort = teamShort;
		this.country = country;
		this.birthday = birthday;
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamShort() {
		return teamShort;
	}

	public void setTeamShort(String teamShort) {
		this.teamShort = teamShort;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String toString() {
		return startNr + "   " + name;
	}

}
