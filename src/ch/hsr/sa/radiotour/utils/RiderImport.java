package ch.hsr.sa.radiotour.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.Team;

public class RiderImport implements ImportIF<Rider> {
	/*
	 * The int values represent the column index in the csvthe indexes
	 * arezerobased
	 */
	private final int startNr = 0;
	private final int name = 1;
	private final int uci = 2;
	private final int team = 4;
	private final int teamShort = 3;

	private final RadioTour app;

	public RiderImport(RadioTour app) {
		this.app = app;
	}

	@Override
	public Rider convertTo(String[] strings) {
		final Rider temp = new Rider();
		final Team tempTeam = app.getTeam(strings[team]) == null ? new Team(
				strings[team]) : app.getTeam(strings[team]);
		tempTeam.setShortName(strings[teamShort]);
		temp.setStartNr(Integer.valueOf(strings[startNr]));
		temp.setBirthday(getBirthday(strings[uci]));
		temp.setCountry(getCountry(strings[uci]));
		temp.setName(strings[name]);
		temp.setTeam(tempTeam);
		return temp;
	}

	private String getCountry(String string) {
		return string.substring(0, 3);
	}

	private Date getBirthday(String string) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		Date temp;
		try {
			temp = formater.parse(string.substring(3));
		} catch (ParseException e) {
			temp = null;
		}
		return temp;
	}
}
