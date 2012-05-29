package ch.hsr.sa.radiotour.technicalservices.importer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import ch.hsr.sa.radiotour.domain.PointOfRace;

public class MarchTableImport implements ImportIF<PointOfRace> {
	/*
	 * The int values represent the column index in the csv the indexes are
	 * zerobased
	 */
	private final int altitude = 0;
	private final int name = 3;
	private final int distance = 1;
	private final int date = 6;

	@Override
	public PointOfRace convertTo(String[] strings) {
		final PointOfRace temp = new PointOfRace();
		temp.setAltitude(Integer.valueOf(strings[altitude]));
		temp.setName(strings[name]);
		temp.setDistance(Double.valueOf(strings[distance]));
		try {
			temp.setEstimatedDate(new SimpleDateFormat("HH:mm")
					.parse(strings[date]));
		} catch (ParseException e) {
			temp.setEstimatedDate(null);
		}
		return temp;
	}
}
