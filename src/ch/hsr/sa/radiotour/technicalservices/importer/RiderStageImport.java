package ch.hsr.sa.radiotour.technicalservices.importer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;

public class RiderStageImport implements ImportIF<RiderStageConnection> {
	/*
	 * The int values represent the column index in the csvthe indexes
	 * arezerobased
	 */
	private final int rank = 0;
	private final int startNr = 1;
	private final int time = 4;

	private final RadioTour app;
	private final SimpleDateFormat formater;
	private final Calendar leaderOfficialTime;

	public RiderStageImport(RadioTour app) {
		this.app = app;
		this.formater = new SimpleDateFormat("HH:mm:ss");
		this.leaderOfficialTime = Calendar.getInstance(TimeZone.getDefault());
	}

	@Override
	public RiderStageConnection convertTo(String[] strings) {
		final RiderStageConnection conn = getConn(strings[startNr]);
		conn.setOfficialRank(Integer.valueOf(strings[rank]));
		Date followerOfficialDeficit = null;
		try {
			followerOfficialDeficit = formater.parse(strings[time]);
		} catch (ParseException e) {
			// FIXME exception handling
		}
		conn.setOfficialDeficit(followerOfficialDeficit);
		conn.setOfficialTime(calculateOfficialTime(followerOfficialDeficit));

		return conn;
	}

	public RiderStageConnection convertFirstTo(String[] strings) {
		final RiderStageConnection conn = app.getRiderStage(Integer
				.valueOf(strings[startNr]));
		conn.setOfficialRank(Integer.valueOf(strings[rank]));
		conn.setOfficialDeficit(new Date(0, 0, 0, 0, 0, 0));
		try {
			leaderOfficialTime.setTime(formater.parse(strings[time]));
		} catch (ParseException e) {
			// FIXME exception handling
		}
		conn.setOfficialTime(leaderOfficialTime.getTime());
		return conn;
	}

	private RiderStageConnection getConn(String s) {
		return app.getRiderStage(Integer.valueOf(s));
	}

	private Date calculateOfficialTime(Date followerOfficialDeficit) {
		Calendar followerOfficialTime = (Calendar) leaderOfficialTime.clone();
		followerOfficialTime.add(Calendar.HOUR_OF_DAY,
				followerOfficialDeficit.getHours());
		followerOfficialTime.add(Calendar.MINUTE,
				followerOfficialDeficit.getMinutes());
		followerOfficialTime.add(Calendar.SECOND,
				followerOfficialDeficit.getSeconds());
		return followerOfficialTime.getTime();
	}
}
