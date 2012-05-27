package ch.hsr.sa.radiotour.utils;

import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.Team;

public class DriverImport implements ImportIF<Rider> {
	/*
	 * The int values represent the column index in the csvthe indexes
	 * arezerobased
	 */
	private final int startNr = 0;
	private final int name = 1;
	private final int team = 2;

	private final RadioTour app;

	public DriverImport(RadioTour app) {
		this.app = app;
	}

	@Override
	public Rider convertTo(String[] strings) {
		final Rider temp = new Rider();
		final Team tempTeam = app.getTeam(strings[team]) == null ? new Team(
				strings[team]) : app.getTeam(strings[team]);
		temp.setStartNr(Integer.valueOf(strings[startNr]));
		temp.setName(strings[name]);
		temp.setTeam(tempTeam);
		return temp;
	}
}
