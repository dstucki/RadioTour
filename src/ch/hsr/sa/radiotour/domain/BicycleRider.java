package ch.hsr.sa.radiotour.domain;

import java.util.Date;

public class BicycleRider {
	private int startNr;
	private String name;
	private String team;
	private String teamShort;
	private String country;
	private String comment;
	private Date official_time;
	private Date official_deficit;
	private Date virtual_deficit;
	private boolean activ;
	private String kategory;
	private String uci;
	private int neo;
	private String language;
	private String url;

	public BicycleRider() {

	}

	public BicycleRider(int startnr, String name, String teamName,
			String teamShort, String country) {
		super();
		this.startNr = startnr;
		this.name = name;
		this.team = teamName;
		this.teamShort = teamShort;
		this.country = country;
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
		return team;
	}

	public void setTeamName(String teamName) {
		this.team = teamName;
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getOfficial_time() {
		return official_time;
	}

	public void setOfficial_time(Date official_time) {
		this.official_time = official_time;
	}

	public Date getOfficial_deficit() {
		return official_deficit;
	}

	public void setOfficial_deficit(Date official_deficit) {
		this.official_deficit = official_deficit;
	}

	public Date getVirtual_deficit() {
		return virtual_deficit;
	}

	public void setVirtual_deficit(Date virtual_deficit) {
		this.virtual_deficit = virtual_deficit;
	}

	public boolean isActiv() {
		return activ;
	}

	public void setActiv(boolean activ) {
		this.activ = activ;
	}

	public String getKategory() {
		return kategory;
	}

	public void setKategory(String kategory) {
		this.kategory = kategory;
	}

	public String getUci() {
		return uci;
	}

	public void setUci(String uci) {
		this.uci = uci;
	}

	public int getNeo() {
		return neo;
	}

	public void setNeo(int neo) {
		this.neo = neo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return startNr + "   " + name;
	}

}
